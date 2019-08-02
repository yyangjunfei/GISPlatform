package cc.wanshan.gis.common.filter;

import cc.wanshan.gis.common.constant.SecurityConstant;
import cc.wanshan.gis.config.properties.TokenProperties;
import cc.wanshan.gis.entity.authorize.TokenUser;
import cc.wanshan.gis.utils.base.ResponseUtil;
import cc.wanshan.gis.utils.token.JwtTokenUtils;
import cc.wanshan.gis.utils.token.SecurityUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RedisTemplate redisTemplate;

    private TokenProperties tokenProperties;

    private SecurityUtils securityUtils;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  RedisTemplate redisTemplate, TokenProperties tokenProperties, SecurityUtils securityUtils) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
        this.tokenProperties = tokenProperties;
        this.securityUtils = securityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(SecurityConstant.TOKEN_HEADER);
        if (header == null || StringUtils.isEmpty(header)) {
            header = request.getParameter(SecurityConstant.TOKEN_HEADER);
        }

        // 如果请求头中没有Authorization信息则直接放行了
        if (header == null || StringUtils.isEmpty(header) || (!tokenProperties.getJwt() && !header.startsWith(SecurityConstant.TOKEN_PREFIX))) {
            chain.doFilter(request, response);
            return;
        }

        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(header, response));
        chain.doFilter(request, response);
    }

    // 这里从token中获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(String header, HttpServletResponse response) {

        String username = null;
        List<GrantedAuthority> authorities = Lists.newArrayList();

        if (tokenProperties.getJwt()) {
            try {
                if (!header.startsWith(SecurityConstant.TOKEN_PREFIX)) {
                    response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                    ResponseUtil.out(response, ResponseUtil.toMap(false, HttpStatus.SC_INTERNAL_SERVER_ERROR, "暂无权限"));
                }
                String token = header.replace(SecurityConstant.TOKEN_PREFIX, "");
                username = JwtTokenUtils.getUsername(token);
                String role = JwtTokenUtils.getUserRole(token);

                logger.info("header::[{}],username;;[{}]", header, username);

                String tokenByRedis = (String) redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + username);

                if (StringUtils.isBlank(tokenByRedis)) {
                    response.setStatus(HttpStatus.SC_FORBIDDEN);
                    ResponseUtil.out(response, ResponseUtil.toMap(false, HttpStatus.SC_FORBIDDEN, "token已失效，请重新登录"));
                }

                if (!tokenByRedis.equals(header)) {
                    response.setStatus(HttpStatus.SC_FORBIDDEN);
                    ResponseUtil.out(response, ResponseUtil.toMap(false, HttpStatus.SC_FORBIDDEN, "token错误，请重新登录"));
                }
                authorities.add(new SimpleGrantedAuthority(role));
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.SC_FORBIDDEN);
                ResponseUtil.out(response, ResponseUtil.toMap(false, HttpStatus.SC_FORBIDDEN, e.getMessage(), "token已过期，请重新登录"));
            } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
                response.setStatus(HttpStatus.SC_FORBIDDEN);
                ResponseUtil.out(response, ResponseUtil.toMap(false, HttpStatus.SC_FORBIDDEN, e.getMessage(), "解析token错误,非法token"));
            } catch (Exception e) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                ResponseUtil.out(response, ResponseUtil.toMap(false, HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage(), "未知错误"));
            }
            return null;
        } else {

            if (tokenProperties.getRedis()) {

                String redisTokenUser = (String) redisTemplate.opsForValue().get(SecurityConstant.TOKEN_USER + header);

                if (redisTokenUser == null || redisTokenUser.isEmpty()) {
                    response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                    ResponseUtil.out(response, ResponseUtil.toMap(false, HttpStatus.SC_UNAUTHORIZED, "登录已失效，请重新登录"));
                    return null;
                }
                TokenUser tokenUser = new Gson().fromJson(redisTokenUser, TokenUser.class);
                username = tokenUser.getUsername();
                authorities.add(new SimpleGrantedAuthority(tokenUser.getRole()));
            } else {
                username = securityUtils.getUserProps(header);
                authorities = securityUtils.getCurrentUserRole(username);
            }
        }

        if (username != null && StringUtils.isNotEmpty(username)) {
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }
        return null;

    }
}

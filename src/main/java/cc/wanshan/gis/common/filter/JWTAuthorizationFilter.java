package cc.wanshan.gis.common.filter;

import cc.wanshan.gis.common.constants.SecurityConstant;
import cc.wanshan.gis.utils.JwtTokenUtils;
import cc.wanshan.gis.utils.base.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private RedisTemplate redisTemplate;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(SecurityConstant.TOKEN_HEADER);
        if (StringUtils.isEmpty(header)) {
            header = request.getParameter(SecurityConstant.TOKEN_HEADER);
        }
        // 如果请求头中没有Authorization信息则直接放行了
        if (header == null || !header.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(header, response));
        chain.doFilter(request, response);
    }

    // 这里从token中获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(String header, HttpServletResponse response) {

        try {
            if (!header.startsWith("Bearer ")) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "暂无权限"));
            }
            String token = header.replace(SecurityConstant.TOKEN_PREFIX, "");
            String username = JwtTokenUtils.getUsername(token);
            String role = JwtTokenUtils.getUserRole(token);

            logger.info("header::[{}],username;;[{}]", header, username);

            String tokenByRedis = (String) redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + username);

            if (tokenByRedis == null) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 401, "token已失效，请重新登录"));
            }

            logger.info("tokenByRedis;;[{}]", tokenByRedis);

            if (!tokenByRedis.equals(header)) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 401, "token错误，请重新登录"));
            }

            return new UsernamePasswordAuthenticationToken(username, null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );
        } catch (ExpiredJwtException e) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 401, "token已过期，请重新登录"));
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 401, "解析token错误,非法token"));
        } catch (Exception ex) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "未知错误"));
        }

        return null;
    }
}

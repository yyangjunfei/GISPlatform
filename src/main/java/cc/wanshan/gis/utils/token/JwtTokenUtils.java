package cc.wanshan.gis.utils.token;

import cc.wanshan.gis.common.constant.SecurityConstant;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

public class JwtTokenUtils {

    public static String createToken(String username, String role) {

        HashMap<String, Object> map = Maps.newHashMap();
        map.put(SecurityConstant.ROLE_CLAIMS, role);
        return Jwts.builder()
                .setClaims(map)
                .setIssuer(SecurityConstant.ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION * 1000))
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.SECRET)
                .compact();
    }

    public static Claims getTokenBody(String token) {

        return Jwts.parser()
                .setSigningKey(SecurityConstant.SECRET)
                .parseClaimsJws(token)
                .getBody();

    }

    /**
     * 获取用户名
     *
     * @param token 密钥
     * @return
     */
    public static String getUsername(String token) {

        return getTokenBody(token).getSubject();

    }

    /**
     * 获取用户角色
     *
     * @param token
     * @return
     */
    public static String getUserRole(String token) {

        return (String) getTokenBody(token).get(SecurityConstant.ROLE_CLAIMS);

    }

    /**
     * 是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {

        return getTokenBody(token).getExpiration().before(new Date());
    }

}

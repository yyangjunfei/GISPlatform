package cc.wanshan.gis.common.constant;

public interface SecurityConstant {

    /**
     * 权限参数头
     */
    String TOKEN_HEADER = "Authorization";
    /**
     * token分割
     */
    String TOKEN_PREFIX = "Bearer ";
    /**
     * SECRET
     */
    String SECRET = "gis-secret";
    /**
     * ISS
     */
    String ISS = "gis";
    /**
     * ROLE_CLAIMS
     */
    String ROLE_CLAIMS = "gis_role";
    /**
     * 过期时间是7200秒，既是2个小时
     */
    long EXPIRATION = 7200L;
    /**
     * 选择了记住我之后的过期时间为7天
     */
    long EXPIRATION_REMEMBER = 604800L;
    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "USER_TOKEN:";
    /**
     * 交互token前缀key
     */
    String TOKEN_USER = "TOKEN_USER:";

}

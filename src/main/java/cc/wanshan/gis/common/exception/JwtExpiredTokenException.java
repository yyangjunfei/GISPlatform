package cc.wanshan.gis.common.exception;

import lombok.Data;

@Data
public class JwtExpiredTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(String token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

}

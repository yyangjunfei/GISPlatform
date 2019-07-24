package cc.wanshan.gis.common.exception;

public class JwtExpiredTokenException extends BaseException {

    private static final long serialVersionUID = 1L;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(String token, String msg, Throwable t) {
        super(msg, t);
    }

}

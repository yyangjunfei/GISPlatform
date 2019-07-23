package cc.wanshan.gis.common.exception;

public class JWTInvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JWTInvalidTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public JWTInvalidTokenException(String msg) {
        super(msg);
    }
}

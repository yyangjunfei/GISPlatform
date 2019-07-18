package cc.wanshan.gis.common.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

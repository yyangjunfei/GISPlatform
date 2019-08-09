package cc.wanshan.gis.common.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author renmaoyan
 * @date 2019/8/5
 * @description TODO
 */

public class CustomAccessDeniedException extends AccessDeniedException {
    private static final long serialVersionUID = 1L;

    public CustomAccessDeniedException(String msg) {
        super(msg);
    }

    public CustomAccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}

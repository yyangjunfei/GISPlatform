package cc.wanshan.gis.common.global;

import cc.wanshan.gis.common.exception.BaseException;
import cc.wanshan.gis.common.exception.CustomAccessDeniedException;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.utils.base.ResultUtil;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(-1)
public class MyControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(MyControllerAdvice.class);

    /**
     * 全局异常捕捉处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception e) {
        logger.error("errorHandler::e = [{}]", e);
        return ResultUtil.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 拦截捕捉自定义异常 BaseException.class
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public Result myErrorHandler(BaseException ex) {
        logger.error("myErrorHandler::ex = [{}]", ex);
        return ResultUtil.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = CustomAccessDeniedException.class)
    public Result myErrorHandler(CustomAccessDeniedException ex) {
        logger.error("myErrorHandler::ex = [{}]", ex);
        return ResultUtil.error(HttpStatus.SC_FORBIDDEN, ex.getMessage());
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public Result myErrorHandler(AccessDeniedException ex) {
        logger.error("myErrorHandler::ex = [{}]", ex);
        return ResultUtil.error(HttpStatus.SC_FORBIDDEN, ex.getMessage() + "123456");
    }
}

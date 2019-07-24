package cc.wanshan.gis.common.global;

import cc.wanshan.gis.common.exception.BaseException;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.utils.base.ResultUtil;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
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
}

package cc.wanshan.gis.config;


import cc.wanshan.gis.entity.MyException;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class MyControllerAdvice {
    private static final Logger logger= LoggerFactory.getLogger(MyControllerAdvice.class);
    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        logger.error("errorHandler::ex = [{}]",ex);
        return ResultUtil.error(4,ex.getMessage());
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MyException.class)
    public Result myErrorHandler(MyException ex) {
        logger.error("myErrorHandler::ex = [{}]",ex);
        return ResultUtil.error(4,ex.getMessage());
    }
}

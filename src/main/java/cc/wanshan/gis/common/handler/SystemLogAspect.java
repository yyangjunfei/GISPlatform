package cc.wanshan.gis.common.handler;

import cc.wanshan.gis.common.annotation.SystemLog;
import cc.wanshan.gis.entity.system.LogInfo;
import cc.wanshan.gis.service.system.LogService;
import cc.wanshan.gis.utils.base.IpUtil;
import cc.wanshan.gis.utils.base.ThreadPoolUtil;
import cc.wanshan.gis.utils.token.SecurityUtils;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author renmaoyan
 * @date 2019/8/13
 * @description Spring AOP实现日志管理
 */
@Slf4j
@Aspect
@Component
public class SystemLogAspect {

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("current start time");

    @Autowired
    private LogService logService;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private SecurityUtils securityUtils;

    /**
     * Controller层切点，注解方式 配置织入点
     */
    @Pointcut("@annotation(cc.wanshan.gis.common.annotation.SystemLog)")
    public void controllerAspect() {

    }

    /**
     * 前置通知
     * 在方法执行之前返回
     * 用于拦截 Controller层记录用户操作的开始时间
     *
     * @param joinPoint
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        //线程绑定变量（数据只对当前请求的线程可见）
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
    }

    /**
     * 后置通知
     * 在方法执行之后返回
     * 用于拦截 Controller层操作
     *
     * @param joinPoint
     */
    @After("controllerAspect()")
    public void doAfter(JoinPoint joinPoint) {
        try {

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (userDetails != null) {

                LogInfo log = new LogInfo();
                log.setId(UUID.randomUUID().toString().replace("-", ""));
                log.setName(getControllerMethodInfo(joinPoint).get("description").toString());
                log.setLogType((Integer) getControllerMethodInfo(joinPoint).get("type"));
                log.setRequestUrl(request.getRequestURI());
                log.setRequestType(request.getMethod());
                log.setRequestParam(new Gson().toJson(request.getParameterMap()));
                log.setUsername(userDetails.getUsername());
                long beginTime = beginTimeThreadLocal.get().getTime();
                long currentTime = System.currentTimeMillis();
                Long costTime = currentTime - beginTime;
                log.setCostTime(costTime.intValue());
                log.setIp(IpUtil.getIpAddr(request));
                log.setAccessTime(new Date());
                //获取浏览器信息
                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                log.setBrowser(userAgent.getBrowser().getName());
                log.setOs(userAgent.getOperatingSystem().getName());

                //调用线程保存到数据库中
                ThreadPoolUtil.getPool().execute(new SaveDbSystemLogThread(log, logService));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws ClassNotFoundException {

        HashMap<String, Object> map = Maps.newHashMap();

        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取参数
        Object[] args = joinPoint.getArgs();
        //生成类对象
        Class<?> targetClass = Class.forName(targetName);
        //获取类中的方法
        Method[] methods = targetClass.getMethods();

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != args.length) {
                continue;
            }
            String description = method.getAnnotation(SystemLog.class).description();
            int type = method.getAnnotation(SystemLog.class).type().ordinal();
            map.put("description", description);
            map.put("type", type);
        }
        return map;
    }

    private static class SaveDbSystemLogThread implements Runnable {

        private LogInfo log;
        private LogService logService;

        public SaveDbSystemLogThread(LogInfo log, LogService logService) {
            this.log = log;
            this.logService = logService;
        }

        @Override
        public void run() {
            logService.insert(log);
        }
    }
}

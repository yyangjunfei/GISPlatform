package cc.wanshan.gis.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识是否忽略登录检查，方法级注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME) //运行时有效
@Documented
public @interface CurrentUser {
}

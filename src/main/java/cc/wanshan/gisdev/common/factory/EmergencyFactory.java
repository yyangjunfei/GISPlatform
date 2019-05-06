package cc.wanshan.gisdev.common.factory;

import java.io.IOException;

/**
 * 应急工厂类
 */
public interface EmergencyFactory<T> {

    T create(String jsonString) throws IOException;
}

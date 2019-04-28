package cc.wanshan.gisdev.common.factory;

import java.io.IOException;

/** 标绘工厂类 */
public interface PlotFactory<T> {
  T create(String jsonString) throws IOException;
}

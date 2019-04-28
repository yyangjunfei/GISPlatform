package cc.wanshan.gisdev.common.factory;

import java.io.IOException;

public interface EmergencyFactory<T> {

  T create(String json) throws IOException;
}

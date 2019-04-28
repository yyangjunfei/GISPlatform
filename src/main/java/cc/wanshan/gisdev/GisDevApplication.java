package cc.wanshan.gisdev;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GisDevApplication {

  @Bean
  public WKTReader wktReader() {
    return new WKTReader();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JtsModule());
    return objectMapper;
  }

  public static void main(String[] args) {
    SpringApplication.run(GisDevApplication.class, args);
  }
}

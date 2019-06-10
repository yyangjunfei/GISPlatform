package cc.wanshan.gis;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * @author Administrator
 */
@SpringBootApplication
public class GISPlatformApplication extends SpringBootServletInitializer {

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
        SpringApplication.run(GISPlatformApplication.class, args);
    }
}

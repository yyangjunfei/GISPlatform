package cc.wanshan.gis.utils;

import cc.wanshan.gis.common.constants.Constant;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 通用工具类
 */
public class CommonUtils {

    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(Constant.split) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(Constant.split));
            }
        }
        return ipAddress;
    }

    public static void GeoLite2IP(String ip) throws Exception {

        //GeoIP2-City 数据库文件
        File database = new ClassPathResource("static/GeoLite2/GeoLite2-City.mmdb").getFile();
        // 创建 DatabaseReader对象
        DatabaseReader reader = null;
        try {
            reader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
            City city = response.getCity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

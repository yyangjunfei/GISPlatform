package cc.wanshan.gis.utils.base;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    /**
     * 使用response输出JSON
     */
    public static void out(HttpServletResponse response, int status, Object data) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(status);
            response.getWriter().write(JSONObject.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用response输出JSON
     */
    public static void out(HttpServletResponse response, Map<String, Object> resultMap) {

        ServletOutputStream out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            out = response.getOutputStream();
            out.write(new Gson().toJson(resultMap).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, Object> toMap(boolean flag, Integer code, String msg) {

        return toMap(flag, code, msg, null);
    }

    public static Map<String, Object> toMap(boolean flag, Integer code, String msg, Object data) {

        Map<String, Object> toMap = new HashMap<>(16);
        toMap.put("success", flag);
        toMap.put("code", code);
        toMap.put("msg", msg);
//        toMap.put("timestamp", System.currentTimeMillis());
        if (data != null) {
            toMap.put("data", data);
        }
        return toMap;
    }

}

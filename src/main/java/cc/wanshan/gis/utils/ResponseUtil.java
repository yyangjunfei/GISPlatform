package cc.wanshan.gis.utils;

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
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "*");
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
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "*");
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

    public static Map<String, Object> resultMap(boolean flag, Integer code, String msg) {

        return resultMap(flag, code, msg, null);
    }

    public static Map<String, Object> resultMap(boolean flag, Integer code, String msg, Object data) {

        Map<String, Object> resultMap = new HashMap<String, Object>(16);
        resultMap.put("success", flag);
        resultMap.put("msg", msg);
        resultMap.put("code", code);
//        resultMap.put("timestamp", System.currentTimeMillis());
        if (data != null) {
            resultMap.put("data", data);
        }
        return resultMap;
    }

}

package cc.wanshan.gisdev.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * jackson 判断字符串是否是JSON
     */
    public static final boolean validate(String jsonString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 转换为字符串
     */
    public static String convertObjectToJSON(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * 转换为字符串
     */
    public static String toJSONNoFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }

    /**
     * 转换为对象
     */
    public static Object toBean(String str) {
        return JSON.parse(str);
    }

    /**
     * 转换为对象
     */
    public static <T> T toBean(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz);
    }

    /**
     * 转换为数组
     */
    public static <T> Object[] toArray(String str) {
        return toArray(str, null);
    }

    /**
     * 转换为数组
     */
    public static <T> Object[] toArray(String str, Class<T> clazz) {
        return JSON.parseArray(str, clazz).toArray();
    }

    /**
     * 转换为List
     */
    public static <T> List<T> toList(String str, Class<T> clazz) {
        return JSON.parseArray(str, clazz);
    }

    /**
     * 将string转化为序列化的json字符串
     */
    public static Object stringToJson(String str) {
        return JSON.parse(str);
    }

    /**
     * json字符串转化为map
     */
    public static <K, V> Map<K, V> stringToCollect(String str) {
        return (Map<K, V>) JSONObject.parseObject(str);
    }

    /**
     * 转换JSON字符串为对象
     */
    public static Object convertJsonToObject(String jsonStr, Class<?> clazz) {
        return JSONObject.parseObject(jsonStr, clazz);
    }

    /**
     * 将map转化为string
     */
    public static <K, V> String collectToString(Map<K, V> map) {
        return JSONObject.toJSONString(map);
    }
}

package cn.com.startai.mqttsdk.utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;

public class SJsonUtils {
    static Gson gson = new Gson();

    public SJsonUtils() {
    }

    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> fromJson(String strJson) {
        try {
            return (Map)gson.fromJson(strJson, Map.class);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        try {
            return gson.fromJson(json, tClass);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static boolean isCorrectJson(String json, Class tClass) {
        Field[] fs = tClass.getDeclaredFields();

        for(int i = 0; i < fs.length; ++i) {
            String name = fs[i].getName();
            if(!json.contains(name)) {
                return false;
            }
        }

        return true;
    }

}

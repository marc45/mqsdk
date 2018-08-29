package cn.com.startai.mqttsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.com.startai.mqttsdk.StartAI;


/**
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class SShareUtils {

    private static String SP_NAME = "startai-mq-sdk";
    private static SharedPreferences sp;


    private static void init() {

        Context context = StartAI.getInstance().getContext();
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

    }

    public static void clear(){
        sp.edit().clear().apply();
    }

    public static void putInt(String key, int value) {
        init();
        sp.edit().putInt(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        init();
        sp.edit().putBoolean(key, value).apply();
    }

    public static void putFloat(String key, float value) {
        init();
        sp.edit().putFloat(key, value).apply();
    }

    public static void putLong(String key, long value) {
        init();
        sp.edit().putLong(key, value).apply();
    }

    public static void putString(String key, String value) {
        init();
        sp.edit().putString(key, value).apply();
    }


    public static String getString(String key, String defaultValue) {
        init();
        return sp.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        init();
        return sp.getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        init();
        return sp.getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        init();
        return sp.getLong(key, defaultValue);

    }

    public static float getFloat(String key, float defaultValue) {
        init();
        return sp.getFloat(key, defaultValue);
    }


}

package cn.com.startai.mqsdk.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by Robin on 2018/7/12.
 * qq: 419109715 彬影
 */

public class FUtils {

    private static String TAG = FUtils.class.getSimpleName();


    /*
     * Java文件操作 获取文件扩展名
     *
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /*
 * Java文件操作 获取不带扩展名的文件名
 */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }


    /**
     * 创建多级目录
     *
     * @param dir nmd/sdcard/startai/download/abc.txt
     */
    public static void mkDir(String dir) throws IOException {

        String[] folder = dir.split("/");
        int length = folder.length;
        String str = "";
        File file;

        for (int i = 0; i < length; i++) {

            str = str + folder[i] + "/";
            file = new File(str);
            if (i == length - 1) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                return;
            }
            if (!file.exists()) {
                file.mkdir();
                TAndL.L(file.getAbsolutePath() + " created !!!");
            } else {
                TAndL.L(file.getAbsolutePath() + " created !!!");
            }

        }

    }
}

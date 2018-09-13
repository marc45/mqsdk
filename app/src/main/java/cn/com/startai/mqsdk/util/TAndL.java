package cn.com.startai.mqsdk.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Robin on 2018/6/22.
 * qq: 419109715 彬影
 */

public class TAndL {

    private static Handler mHandler = new android.os.Handler(Looper.getMainLooper());

    public static final String TAG = "TAndL";
    private static Handler hd;
    private static PrintWriter pwTemp;
    private static Toast toast;
    private int title;

    public static void L(String text) {
        Log.i(TAG, text);
    }

    public static void T(final Context context, final String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {

                    toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                } else {

                    toast.setText(text);
                }
                toast.show();

            }
        });
    }

    public static void TL(Context context, String text) {

        L(text);
        T(context, text);

    }


    public static void file(String tag, String content) {
        file(new String[]{tag}, new String[]{content});
    }

    public static void file(final String[] tag, final String[] content) {

        if (hd == null) {
            HandlerThread ht = new HandlerThread(TAG);
            ht.start();
            hd = new Handler(ht.getLooper());
        }
        hd.post(new Runnable() {

            @Override
            public void run() {


                try {
                    long t = System.currentTimeMillis();
                    File logFile = getLogFile();
                    long t2 = System.currentTimeMillis();
                    Log.i(TAG, "get log file use time = " + (t2 - t));
                    if (logFile == null) {
                        return;
                    }

                    OutputStream out = new FileOutputStream(logFile, true);
                    pwTemp = new PrintWriter(out, true);
                    if (FileUtils.getFileLength(logFile) <= 0) {
                        pwTemp.write(getTitle());
                    }
                    pwTemp.write(combineData(tag, content));
                    pwTemp.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (pwTemp != null) {
                        pwTemp.close();
                    }
                }

            }
        });


    }

    public static void file(final Exception ex) {
        if (hd == null) {
            HandlerThread ht = new HandlerThread(TAG);
            ht.start();
            hd = new Handler(ht.getLooper());
        }
        hd.post(new Runnable() {
            @Override
            public void run() {


                PrintStream psTemp = null;
                try {


                    File file = getLogFile();
                    if (file == null) {
                        return;
                    }
                    //创建一个临时文件
                    OutputStream out = new FileOutputStream(file, true);

                    psTemp = new PrintStream(out, true);
                    psTemp.print(initHeadData(true));
                    ex.printStackTrace(psTemp);


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (psTemp != null) {
                        psTemp.close();
                    }
                }
            }
        });
    }


    static String initHeadData(boolean isException) {

        return "\n" + "AppVersion:"
                + AppUtils.getAppInfo().getVersionName() + "\n" + "Time:"
                + com.blankj.utilcode.util.TimeUtils.date2String(new Date(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SSS")) + "\n" + (isException ? "Exception:" + "\n" : "");
    }

    private static String combineData(String[] tags, String[] contents) {

        String data = "";

        for (int i = 0; i < tags.length; i++) {
            if (TextUtils.isEmpty(contents[i])) {
                continue;
            }
            data = data + tags[i] + ":\n  " + contents[i] + "\n";
        }
        data = data + "\n";


        String head = initHeadData(false);
        data = head + data;
        return data;

    }


    private static final String logDir = "startai/mqsdk/log/mqsdk_";
    private static final String logExt = ".log";


    private static File getLogFile() {

        File finalFile = null;

        String yyyyMMdd = com.blankj.utilcode.util.TimeUtils.getNowString(new SimpleDateFormat("yyyyMMdd"));
        String yyyyMMddHH = com.blankj.utilcode.util.TimeUtils.getNowString(new SimpleDateFormat("yyyyMMddHH"));


        File file = new File(Environment.getExternalStorageDirectory(), logDir + yyyyMMdd + logExt);

        if (file.exists()) {
            if (file.length() < 1024 * 1024 * 5) {
                return file;
            } else {
                //大于5M 创建新的文件
                FileUtils.rename(file, file.getName().replace(yyyyMMdd, yyyyMMddHH));
            }
        }

        finalFile = new File(Environment.getExternalStorageDirectory(), logDir + yyyyMMdd + logExt);
        try {
            FUtils.mkDir(finalFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalFile;

    }


    private static String getTitle() {
        String sdk = Build.VERSION.RELEASE;// android版本号
        String model = Build.MODEL;// 设备型号
        return "\n" + "model:" + model + "\n" + "android Version:" + sdk + "\n" + "product:" + Build.PRODUCT + "\n";

    }

}

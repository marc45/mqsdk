package cn.com.startai.mqttsdk.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.startai.mqttsdk.listener.SLogListener;
import cn.com.startai.mqttsdk.mqtt.StartaiMqttPersistent;


/**
 * Created by Robin on 2018/5/3.
 * qq: 419109715 彬影
 */

public class SLog {

    private static boolean isDebug = true;
    private static Handler mHandler;

    //    private Context context = StartAI.init().getContext();
    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    private SLog() {
    }

    public static void json(String TAG, Object object) {
        d(TAG, SJsonUtils.toJson(object));
    }


    private static void callbackLog(String tag, String content) {
//        SLogListener slogListener = getSlogListener();
//        if (slogListener != null) {
//            slogListener.onLog(tag, content);
//        }
    }

//    private static SLogListener getSlogListener() {
//
//        StartaiMqttPersistent instance =  StartaiMqttPersistent.getInstance();
//        SLogListener sLogListener = instance.getParams().getsLogListener();
//        return sLogListener;
//
//
//    }


    public static void d(String tag, String logMsg) {
        if (isDebug) {
            Log.d(tag, logMsg);
            callbackLog(tag, logMsg);
            file(tag, logMsg);
        }
    }

//    public static void i(String tag, String logMsg) {
//        if (isDebug) {
//            Log.i(tag, logMsg);
//            callbackLog(tag, logMsg);
//            file(tag, logMsg);
//        }
//    }

    public static void w(String tag, String logMsg) {
        if (isDebug) {
            Log.w(tag, logMsg);
            callbackLog(tag, logMsg);
            file(tag, logMsg);
        }
    }

    public static void e(String tag, String logMsg) {
        if (isDebug) {
            Log.e(tag, logMsg);
            callbackLog(tag, logMsg);
            file(tag, logMsg);
        }
    }


    public static synchronized void file(final String tags, final String contents) {
//        file(new String[]{tags}, new String[]{contents});
    }

    private static String FORMATE_TS = "yyyy-MM-dd HH:mm:ss_SSS";
    private static String FORMATE_DATE = "yyyy-MM-dd";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATE_DATE);
    private static SimpleDateFormat tsFormat = new SimpleDateFormat(FORMATE_TS);
    private static String LOG_FILE = "";

//    /**
//     * 将log信息写入文件中
//     */
//    private static synchronized void file(final String[] tags, final String[] contents) {
//
//        if (mHandler == null) {
//            HandlerThread ht = new HandlerThread(TAG);
//            ht.start();
//            mHandler = new Handler();
//        }
//
//        mHandler.post(new Runnable() {
//            @Override
//            public synchronized void run() {
//
//                File file = getLogFile();
//
//                if (tags == null || contents == null || tags.length != contents.length) {
//                    d(TAG, "日志参数非法");
//                    return;
//                }
//
//                String data = "";
//                for (int i = 0; i < tags.length; i++) {
//                    if (TextUtils.isEmpty(contents[i])) {
//                        continue;
//                    }
//                    data = data + tags[i] + ":\n  " + contents[i] + "\n";
//                }
//
//                String currDateStr = tsFormat.format(new Date());
//
//                data = currDateStr + "\n" + data + "\n";
//
//                FileOutputStream fos = null;//FileOutputStream会自动调用底层的close()方法，不用关闭
//                BufferedWriter bw = null;
//                try {
//
//                    fos = new FileOutputStream(file, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
//                    bw = new BufferedWriter(new OutputStreamWriter(fos));
//                    bw.write(data);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (bw != null) {
//                            bw.close();//关闭缓冲流
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//    }


//    /**
//     * 保存日志
//     *
//     * @param ex
//     */
//    public static void file(final Exception ex) {
//        if (mHandler == null) {
//            HandlerThread th = new HandlerThread("MyLog");
//            th.start();
//            mHandler = new Handler(th.getLooper());
//        }
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                long time = System.currentTimeMillis();
//                String format = dateFormat.format(FORMATE_DATE);
//
//                File logPath = new File(Environment.getExternalStorageDirectory(), "startai");
//                String fileName = logPath + "/log/log_" + dateFormat.format(new Date()) + ".log";//log日志名，使用时间命名，保证不重复
//
//
//                PrintStream psTemp = null;
//                try {
//
////                    File file = new File(FileUtil.getSdcardPath(context) + "/startai/log/log_"
////                            + NMCDateUtil.getCurrentDate("yyyyMMdd") + ".txt");
////                    if (!file.getParentFile().exists()) {
////                        file.getParentFile().mkdirs();
////                    }
////                    if (!file.exists()) {
////                        file.createNewFile();
////
////                    }
//
//                    File file = getLogFile();
//                    if (file == null) {
//                        return;
//                    }
//
//                    OutputStream out = new FileOutputStream(file, true);
//
//                    psTemp = new PrintStream(out, true);
////                    psTemp.print(initExceptionData(context));
//                    ex.printStackTrace(psTemp);
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
////                    MyLog.e("MyLog", e.getMessage());
//                } finally {
//                    if (psTemp != null) {
//                        psTemp.close();
//                    }
//                }
//            }
//
//        });
//    }
//

//    private static File getLogFile() {
//
//
//        File logPath = new File(Environment.getExternalStorageDirectory(), "startai");
//        String fileName = logPath + "/log/log_" + dateFormat.format(new Date()) + ".log";//log日志名，使用时间命名，保证不重复
//
//
//        File file = new File(fileName);
//        if (!file.exists()) {
//            file.getParentFile().getParentFile().getParentFile().mkdirs();
//            file.getParentFile().getParentFile().mkdirs();
//            file.getParentFile().mkdirs();
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        if (file.length() > 5 * 1024 * 1024) {
//            String oldPath = file.getAbsolutePath();
//            String hHmm = new SimpleDateFormat("HHmm").format(new Date());
//            String newPath = oldPath.substring(0, oldPath.lastIndexOf(".log")) + hHmm + ".log";
//
//            file.renameTo(new File(newPath));
//
//            File fileResult = new File(oldPath);
//            try {
//                fileResult.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return fileResult;
//        } else {
//            return file;
//        }
//
//    }

}

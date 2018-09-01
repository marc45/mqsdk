package cn.com.startai.mqsdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Robin on 2018/6/22.
 * qq: 419109715 彬影
 */

public class MyService extends Service {


    private String TAG = MyService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        initMqSdk();

        return super.onStartCommand(intent, flags, startId);


    }

    private void initMqSdk() {




    }


}

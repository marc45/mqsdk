package cn.com.startai.mqsdk.util;

import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {

    private int colorDefault;
    private Button btn;

    public TimeCount(long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
    }

    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(final long millisUntilFinished) {

//        btn.setBackgroundColor(Color.parseColor("#B6B6D8"));
        btn.setClickable(false);
        btn.setText("(" + millisUntilFinished / 1000 + ")重新发送");
    }

    @Override
    public void onFinish() {
        btn.setText("获取验证码");
        btn.setClickable(true);
//        btn.setBackgroundColor(Color.parseColor("#808080"));
    }
}
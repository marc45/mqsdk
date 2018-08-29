package cn.com.startai.mqsdk.util.zxing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerOptions;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.decode.QRDecode;

import cn.com.startai.mqsdk.R;


/**
 * Created by Robin on 2018/3/1.
 * qq: 419109715 彬影
 */

public class ScanActivity extends AppCompatActivity implements
        OnScannerCompletionListener {

    public static final String EXTRA_LASER_LINE_MODE = "extra_laser_line_mode";
    public static final String EXTRA_SCAN_MODE = "extra_scan_mode";
    public static final String EXTRA_SHOW_THUMBNAIL = "EXTRA_SHOW_THUMBNAIL";
    public static final String EXTRA_SCAN_FULL_SCREEN = "EXTRA_SCAN_FULL_SCREEN";
    public static final String EXTRA_HIDE_LASER_FRAME = "EXTRA_HIDE_LASER_FRAME";


    public static final int EXTRA_LASER_LINE_MODE_0 = 0;//线条图
    public static final int EXTRA_LASER_LINE_MODE_1 = 1;//网格图
    public static final int EXTRA_LASER_LINE_MODE_2 = 2;//线


    public static final int EXTRA_SCAN_MODE_0 = 0;//全部
    public static final int EXTRA_SCAN_MODE_1 = 1;//仅二维
    public static final int EXTRA_SCAN_MODE_2 = 2;//仅一维

    public static final int APPLY_READ_EXTERNAL_STORAGE = 0x111;

    private ScannerView mScannerView;
    private Result mLastResult;
    private String TAG = ScanActivity.class.getSimpleName();


    private boolean showThumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.this.finish();
            }
        });


        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mScannerView.toggleLight(isChecked);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ScanActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            APPLY_READ_EXTERNAL_STORAGE);


                } else {
                    chooseFromAlbum();
                }
            }
        });





        mScannerView = (ScannerView) findViewById(R.id.scanner_view);

        mScannerView.setOnScannerCompletionListener(this);

//        mScannerView.toggleLight(true);

        ScannerOptions.Builder builder = new ScannerOptions.Builder();
        builder.setFrameSize(256, 256)
                .setFrameCornerLength(22)
                .setFrameCornerWidth(2)
                .setFrameCornerInside(true)

//                .setLaserLineColor(0xff06c1ae)
//                .setLaserLineHeight(18)

                .setLaserStyle(ScannerOptions.LaserStyle.RES_LINE, R.mipmap.wx_scan_line)
//                .setLaserStyle(ScannerOptions.LaserStyle.RES_GRID, R.mipmap.zfb_grid_scan_line)//网格图

                .setFrameCornerColor(0xFF99cc33)//扫描框颜色

                .setScanFullScreen(false)

                .setFrameHide(true) //扫描框边线
//                .setFrameCornerHide(false)
//                .setLaserMoveFullScreen(false)
//                .setFrameOutsideColor(0xFF808080)
//                .setViewfinderCallback(new ScannerOptions.ViewfinderCallback() {
//                    @Override
//                    public void onDraw(View view, Canvas canvas, Rect frame) {
//                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.connect_logo);
//                        canvas.drawBitmap(bmp, frame.right / 2, frame.top - bmp.getHeight(), null);
//                    }
//                })
                .setMediaResId(R.raw.baidu_beep)
                .setScanMode(BarcodeFormat.QR_CODE)
                .setTipText("将二维码放入框内")
                .setTipTextSize(16)
                .setTipTextColor(getResources().getColor(R.color.color_gray))
//                .setCameraZoomRatio(2)
        ;

        mScannerView.setScannerOptions(builder.build());

    }

    @Override
    public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {

        if (rawResult == null) {
            Toast.makeText(this, "未发现二维码", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

//        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
//        vibrate();
        Log.i(TAG, "扫描到二维码 " + rawResult.getText());

        mScannerView.restartPreviewAfterDelay(1000);

        Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
        intent.putExtra("result", rawResult.getText());
        setResult(RESULT_OK, intent);
        finish();

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    @Override
    protected void onResume() {
        mScannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mLastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void restartPreviewAfterDelay(long delayMS) {
        mScannerView.restartPreviewAfterDelay(delayMS);
    }


    /**
     * 申请 查看本地相册权限 结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == APPLY_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //选择相册
                chooseFromAlbum();
            } else {
                Toast.makeText(ScanActivity.this, "请给予权限", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * 选择完相册图片返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                String photo_path = "";
                String[] proj = {MediaStore.Images.Media.DATA};
                // 获取选中图片的路径
                Cursor cursor = getContentResolver().query(data.getData(), proj, null,
                        null, null);

                if (cursor.moveToFirst()) {

                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    photo_path = cursor.getString(column_index);
                    if (photo_path == null) {
                        photo_path = Utils.getPath(getApplicationContext(),
                                data.getData());
                        Log.i("123path  Utils", photo_path);
                    }
                    Log.i("123path", photo_path);

                }
                cursor.close();

                QRDecode.decodeQR(photo_path, this);
            }
        }
    }


    /**
     * 从相册选择二维码图片
     */
    protected void chooseFromAlbum() {

        Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
        // 在android 4.4 以后的系统版本中 打开系统相册的意图有改动，此处要作判断
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }

        innerIntent.setType("image/*");

        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");

        startActivityForResult(wrapperIntent, 1);

    }

    public static void showActivityForResult(Activity act, int requestCode) {
        Intent intent = new Intent(act, ScanActivity.class);
        act.startActivityForResult(intent, requestCode);
    }

}

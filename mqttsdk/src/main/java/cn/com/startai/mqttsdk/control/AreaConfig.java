package cn.com.startai.mqttsdk.control;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.com.startai.mqttsdk.control.entity.AreaLocation;
import cn.com.startai.mqttsdk.utils.SJsonUtils;
import cn.com.startai.mqttsdk.utils.SLog;

import static cn.com.startai.mqttsdk.StartAI.TAG;

/**
 * 节点地区
 * Created by Robin on 2018/5/11.
 * qq: 419109715 彬影
 */

public class AreaConfig {


    //{"as":"AS4134 No.31,Jin-rong Street","city":"Guangzhou","country":"China","countryCode":"CN","isp":"China Telecom Guangdong","lat":23.1167,"lon":113.25,"org":"China Telecom","query":"59.42.207.3","region":"GD","regionName":"Guangdong","status":"success","timezone":"Asia/Shanghai","zip":""}
    private static String url = "http://ip-api.com/json/";
//    private static String url = "http://192.168.1.206:8079/service/getipinfo";
//        String url = "http://jinjian.mynatapp.cc/service/getIp";


    /**
     * 获取位置信息接口
     *
     * @return
     */
    public static AreaLocation getArea() {
        AreaLocation areaLocation = null;
        URL infoUrl = null;
        InputStream inStream = null;
        HttpURLConnection httpConnection = null;
        BufferedReader reader = null;

        String var5;
        try {
            infoUrl = new URL(url);

            URLConnection urlConnection = infoUrl.openConnection();
            urlConnection.setConnectTimeout(4000);
            urlConnection.setReadTimeout(4000);
            httpConnection = (HttpURLConnection) urlConnection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }

            inStream = httpConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                strber.append(line + "\n");
            }
            inStream.close();

            SLog.d(TAG, "定位结果 " + strber.toString());
            areaLocation = SJsonUtils.fromJson(strber.toString(), AreaLocation.class);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

                if (inStream != null) {
                    inStream.close();
                }

                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (areaLocation != null) {
            SPController.setLocation(areaLocation);
        }
        return areaLocation;

    }

}

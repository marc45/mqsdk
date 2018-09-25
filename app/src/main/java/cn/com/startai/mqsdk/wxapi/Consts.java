package cn.com.startai.mqsdk.wxapi;

/**
 * Created by Robin on 2018/8/21.
 * qq: 419109715 彬影
 */

public class Consts {

    public static final String APP_ID = "wx06b791bccc38c10b";

    //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code

    public static final String URL_GET_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    //https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
    public static final String URL_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
    //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
    public static final String URL_GET_SUSERINFO = "https://api.weixin.qq.com/sns/userinfo?";


}

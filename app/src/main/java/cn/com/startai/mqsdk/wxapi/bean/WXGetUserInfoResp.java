package cn.com.startai.mqsdk.wxapi.bean;

import java.util.List;

/**
 * Created by Robin on 2018/8/21.
 * qq: 419109715 彬影
 */

public class WXGetUserInfoResp<T> {


    /**
     * openid : olmt4wfxS24VeeVX16_zUhZezY
     * nickname : 李文星
     * sex : 1
     * language : zh_CN
     * city : Shenzhen
     * province : Guangdong
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/ajNVdqHZLLDickRibe5D4x2ADgSfianmA4kK9hY4esrvGhmAFCe5wjox6b6pL4ibiblKnxibzVtGdqfa2UVHACfmmUsQ/0
     * privilege : []
     * unionid : o5aWQwAa7niCIXhAIRBOwglIJ7UQ
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<T> privilege;

    @Override
    public String toString() {
        return "WXGetUserInfoResp{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                ", privilege=" + privilege +
                '}';
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<T> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<T> privilege) {
        this.privilege = privilege;
    }
}

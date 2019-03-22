package cn.com.startai.mqttsdk.localbusi;

import cn.com.startai.mqttsdk.busi.entity.C_0x8018;
import cn.com.startai.mqttsdk.control.SDBmanager;
import cn.com.startai.mqttsdk.control.SPController;
import cn.com.startai.mqttsdk.control.entity.UserBean;

/**
 * 用户 业务 中间类
 * Created by Robin on 2018/8/13.
 * qq: 419109715 彬影
 */

public class UserBusi {




    /**
     * 获取当前登录的用户
     *
     * @return
     */
    public C_0x8018.Resp.ContentBean getCurrUser() {


        UserBean currUserFromDb = getCurrUserFromDb();
        if (currUserFromDb != null) {
            C_0x8018.Resp.ContentBean contentBean = new C_0x8018.Resp.ContentBean(currUserFromDb.getUserid(), currUserFromDb.getToken(), currUserFromDb.getExpire_in(), currUserFromDb.getUName(), currUserFromDb.getType());
            //清空sp保存的user
            SPController.setUserInfo(null);
            return contentBean;
        }


        C_0x8018.Resp.ContentBean currUserFromSp = getCurrUserFromSp();

        if (currUserFromSp == null) {
            return null;
        }

        UserBean userBean = new UserBean(currUserFromSp.getUserid(), currUserFromSp.getToken(), currUserFromSp.getExpire_in(), currUserFromSp.getuName(), 0, 1);
        SDBmanager.getInstance().addOrUpdateUser(userBean);
        SPController.setUserInfo(null);
        return currUserFromSp;

    }

    public String getUserId() {
        String userId = "";
        C_0x8018.Resp.ContentBean currUser = getCurrUser();
        if (currUser != null) {
            userId = currUser.getUserid();
        }
        return userId;
    }


    public UserBean getCurrUserFromDb() {

        UserBean u = SDBmanager.getInstance().getUserByLoginStatus(1);

        return u;

    }

    public C_0x8018.Resp.ContentBean getCurrUserFromSp() {
        C_0x8018.Resp.ContentBean userInfo = SPController.getUserInfo();

        return userInfo;

    }


    public void resetDBUser() {
        SDBmanager.getInstance().resetUser();
    }


}

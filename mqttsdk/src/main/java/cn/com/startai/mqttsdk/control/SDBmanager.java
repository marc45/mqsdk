package cn.com.startai.mqttsdk.control;

import java.util.ArrayList;

import cn.com.startai.mqttsdk.StartAI;
import cn.com.startai.mqttsdk.control.entity.MsgWillSendBean;
import cn.com.startai.mqttsdk.control.entity.TopicBean;
import cn.com.startai.mqttsdk.control.entity.UserBean;
import cn.com.startai.mqttsdk.db.DbException;
import cn.com.startai.mqttsdk.db.DbManager;
import cn.com.startai.mqttsdk.db.DbManagerImpl;
import cn.com.startai.mqttsdk.db.KeyValue;
import cn.com.startai.mqttsdk.db.sqlite.WhereBuilder;
import cn.com.startai.mqttsdk.utils.SLog;

/**
 * 数据库管理类
 * Created by Robin on 2018/7/17.
 * qq: 419109715 彬影
 */

public class SDBmanager {

    private static SDBmanager instance;
    private DbManager.DaoConfig daoConfig;
    private static DbManager db;
    private final String DB_NAME = "qxsdk.db";
    private static final int VERSION = 2;
    private static String TAG = SDBmanager.class.getSimpleName();


    private SDBmanager() {
        SLog.d(TAG, "准备打开数据库");
        this.daoConfig = (new DbManager.DaoConfig())
                .setDbName(DB_NAME)
                .setDbVersion(VERSION)
                .setAllowTransaction(true)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                        SLog.d(SDBmanager.TAG, "数据库已经打开 name = " + DB_NAME + " verison = " + VERSION);
                    }
                }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        SLog.d(TAG, "数据库升级" + oldVersion + " ==> " + newVersion);
                    }
                });
        db = DbManagerImpl.getInstance(StartAI.getContext(), this.daoConfig);
    }

    public synchronized DbManager getDB() {
        return db;
    }

    public static SDBmanager getInstance() {
        if (instance == null) {
            instance = new SDBmanager();
        }
        return instance;
    }

    public void deleteAllDB() {
        try {
            db.dropDb();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    /**
     * 查询所有
     *
     * @param id userid/sn
     * @return
     */
    public ArrayList<TopicBean> getAllTopic(String id) {

        try {
            ArrayList<TopicBean> all = (ArrayList<TopicBean>) db.selector(TopicBean.class).where(WhereBuilder.b(TopicBean.F_ID, "=", id)).findAll();
            return all;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 删除所有
     *
     * @param id userid/sn
     */
    public void deleteAllTopic(String id) {

        try {
            db.delete(TopicBean.class, WhereBuilder.b(TopicBean.F_ID, "=", id));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除所有
     */
    public void deleteAllTopic() {

        try {
            db.delete(TopicBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加或更新
     *
     * @param topicBean
     */
    public void addOrUpdateTopic(TopicBean topicBean) {
        long t = System.currentTimeMillis();
        try {
            TopicBean first = db.selector(TopicBean.class).where(WhereBuilder.b(TopicBean.F_ID, "=", topicBean.getId()).and(TopicBean.F_TOPIC, "=", topicBean.getTopic())).findFirst();
            topicBean.setTime(System.currentTimeMillis());
            if (first == null) {
                db.save(topicBean);
            } else {
                topicBean.set_id(first.get_id());
                db.update(topicBean);
            }
            SLog.d(TAG, "addOrUpdate use time = " + (System.currentTimeMillis() - t));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置
     */
    public void resetTopic(String id) {
        try {
            db.update(TopicBean.class, WhereBuilder.b(TopicBean.F_ID, "=", id), new KeyValue(TopicBean.F_TYPE, "remove"));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除
     *
     * @param id
     * @param topic
     */
    public void deleteTopicByTopic(String id, String topic) {
        try {
            db.delete(TopicBean.class, WhereBuilder.b(TopicBean.F_ID, "=", id).and(TopicBean.F_TOPIC, "=", topic));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询所有
     */
    public ArrayList<UserBean> getAllUser() {
        ArrayList<UserBean> all = null;
        try {
            all = (ArrayList<UserBean>) db.findAll(UserBean.class);

        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        if (all == null) {
            all = new ArrayList<>();
        }
        return all;
    }

    /**
     * 删除所有
     */
    public void deleteAllUser() {

        try {
            db.delete(UserBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除单个
     */
    public void deleteUserByUserid(String userid) {

        try {
            db.delete(UserBean.class, WhereBuilder.b(UserBean.F_USERID, "=", userid));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除单个
     */
    public void deleteUserByUName(String uName) {

        try {
            db.delete(UserBean.class, WhereBuilder.b(UserBean.F_UNAME, "=", uName));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除单个
     */
    public void deleteUserByLoginStatus(int loginStatus) {

        try {
            db.delete(UserBean.class, WhereBuilder.b(UserBean.F_LOGINSTATUS, "=", loginStatus));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    /**
     * 添加或更新
     *
     * @param userBean
     */
    public void addOrUpdateUser(UserBean userBean) {
        long t = System.currentTimeMillis();
        try {
            UserBean first = db.selector(UserBean.class).where(WhereBuilder.b(UserBean.F_USERID, "=", userBean.getUserid())).findFirst();
            userBean.setTime(System.currentTimeMillis());
            if (first == null) {
                db.save(userBean);
            } else {
                userBean.set_id(first.get_id());
                db.update(userBean);
            }
            SLog.d(TAG, "addOrUpdate use time = " + (System.currentTimeMillis() - t));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置
     */
    public void resetUser() {
        try {
            db.update(UserBean.class, null, new KeyValue(UserBean.F_LOGINSTATUS, 0));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public UserBean getUserByUserid(String userid) {

        UserBean first = null;
        try {
            first = db.selector(UserBean.class).where(UserBean.F_USERID, "=", userid).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return first;

    }

    public UserBean getUserByUname(String uName) {
        UserBean first = null;
        try {
            first = db.selector(UserBean.class).where(UserBean.F_UNAME, "=", uName).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return first;
    }

    public UserBean getUserByLoginStatus(int loginStatus) {
        UserBean first = null;
        try {
            first = db.selector(UserBean.class).where(UserBean.F_LOGINSTATUS, "=", loginStatus).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        SLog.d(TAG, "db currUser = " + first);
        return first;
    }


    /**
     * 添加或更新
     *
     * @param
     */
    public void addOrUpdateMsgWillSend(MsgWillSendBean bean) {
        long t = System.currentTimeMillis();
        try {
            MsgWillSendBean first = db.selector(MsgWillSendBean.class).where(WhereBuilder.b(MsgWillSendBean.FIELD_MSGTYPE, "=", bean.getMsgtype())).findFirst();
            bean.setTime(System.currentTimeMillis());
            if (first == null) {
                db.save(bean);
            } else {
                bean.set_id(first.get_id());
                db.update(bean);
            }
            SLog.d(TAG, "addOrUpdate use time = " + (System.currentTimeMillis() - t));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void deleteMsgWillSendByMsgType(String msgtype) {

        try {
            db.delete(MsgWillSendBean.class, WhereBuilder.b(MsgWillSendBean.FIELD_MSGTYPE, "=", msgtype));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public void deleteMsgWillSendByMsg(String msg) {

        try {
            db.delete(MsgWillSendBean.class, WhereBuilder.b(MsgWillSendBean.FIELD_MSGWILLSEND, "=", msg));
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public MsgWillSendBean getMsgWillSendByMsgtype(String msgType) {
        MsgWillSendBean first = null;
        try {
            first = db.selector(MsgWillSendBean.class).where(MsgWillSendBean.FIELD_MSGTYPE, "=", msgType).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return first;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public ArrayList<MsgWillSendBean> getAllMsgWillSend() {
        ArrayList<MsgWillSendBean> all = null;

        try {
            all = (ArrayList<MsgWillSendBean>) db.selector(MsgWillSendBean.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (all == null) {
            all = new ArrayList<>();
        }
        return all;
    }

}

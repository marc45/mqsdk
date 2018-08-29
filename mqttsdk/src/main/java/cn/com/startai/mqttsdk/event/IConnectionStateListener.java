package cn.com.startai.mqttsdk.event;

public interface IConnectionStateListener {

    void onConnectFail(int errorCode, String errorMsg);

    void onConnected();

    void onDisconnect(int errorCode, String errorMsg);

    /**
     * 是否回调到ui线程
     *
     * @return
     */
    boolean needUISafety();

}

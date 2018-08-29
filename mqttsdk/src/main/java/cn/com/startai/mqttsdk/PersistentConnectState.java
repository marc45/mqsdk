package cn.com.startai.mqttsdk;

public enum PersistentConnectState {
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
    DISCONNECTING,
    CONNECTFAIL;

    private PersistentConnectState() {
    }

}
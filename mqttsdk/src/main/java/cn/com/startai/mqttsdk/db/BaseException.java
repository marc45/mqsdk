
package cn.com.startai.mqttsdk.db;

import java.io.IOException;

public class BaseException extends IOException {
    private static final long serialVersionUID = 1L;

    public BaseException() {
        super();
    }

    public BaseException(String detailMessage) {
        super(detailMessage);
    }

    public BaseException(String detailMessage, Throwable throwable) {
        super(detailMessage);
        this.initCause(throwable);
    }

    public BaseException(Throwable throwable) {
        super(throwable.getMessage());
        this.initCause(throwable);
    }
}

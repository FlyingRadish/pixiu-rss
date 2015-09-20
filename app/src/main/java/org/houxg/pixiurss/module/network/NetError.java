package org.houxg.pixiurss.module.network;

/**
 * desc:网络错误异常
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/4
 */
public class NetError extends Exception {

    public NetError(String detailMessage) {
        super(detailMessage);
    }

    public NetError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

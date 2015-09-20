package org.houxg.pixiurss.utils;

/**
 * desc:可取消的Runnable
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/15
 */
public interface CancelRunnable extends Runnable {
    void cancel();
}

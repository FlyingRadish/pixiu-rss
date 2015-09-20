package org.houxg.pixiurss.module.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/4
 */
public class NetWorker extends Thread {

    BlockingQueue<NetTask> currentTasks;
    OkHttpClient client;

    public NetWorker() {
        currentTasks = new ArrayBlockingQueue<NetTask>(16);
        client = new OkHttpClient();
    }

    @Override
    public void run() {

        while (true) {
            try {
                NetTask handleTask = currentTasks.take();


            } catch (InterruptedException e) {

            }
        }
    }
}

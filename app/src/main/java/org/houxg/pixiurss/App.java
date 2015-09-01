package org.houxg.pixiurss;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by houxg on 2015/8/24.
 */
public class App extends Application {
    private static OkHttpClient client;

    public static OkHttpClient getOKHttpClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }
}

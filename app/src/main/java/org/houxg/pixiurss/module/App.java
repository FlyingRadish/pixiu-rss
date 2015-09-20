package org.houxg.pixiurss.module;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import net.danlew.android.joda.JodaTimeAndroid;

import org.houxg.pixiurss.DaoMaster;
import org.houxg.pixiurss.DaoSession;

/**
 * Created by houxg on 2015/8/24.
 */
public class App extends Application {
    private static Context ctx;
    private static OkHttpClient client;

    public static OkHttpClient getOKHttpClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    static DaoMaster daoMaster;
    static DaoSession daoSession;

    public static DaoMaster getMaster() {
        if (daoMaster == null) {
            DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(ctx, "data", null);
            SQLiteDatabase database = openHelper.getWritableDatabase();
            daoMaster = new DaoMaster(database);
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getMaster().newSession();
        }
        return daoSession;
    }

    public static void ThreadPoolExecut(Runnable runnable) {
        Log.i("h", "hei");
        new Thread(runnable).start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        Log.i("houxg", "init");
        JodaTimeAndroid.init(this);
        Log.i("houxg", "init end");
    }
}

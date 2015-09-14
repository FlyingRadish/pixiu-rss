package org.houxg.pixiurss.utils.toolbox;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by houxg on 2015/7/10.
 */
public class OpenTool {

    public static void startActivity(Context ctx, Class<?> cls) {
        startActivity(ctx, cls, null, null);
    }

    public static void startActivity(Context ctx, Class<?> cls, String action) {
        startActivity(ctx, cls, action, null);
    }

    public static void startActivity(Context ctx, Class<?> cls, Bundle data) {
        startActivity(ctx, cls, null, data);
    }

    public static void startActivity(Context ctx, Class<?> cls, String action, Bundle data) {
        Intent intent = new Intent(ctx, cls);
        if (action != null) {
            intent.setAction(action);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        ctx.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class<?> cls, int requestCode) {
        startActivityForResult(activity, cls, requestCode, null, null);
    }

    public static void startActivityForResult(Activity activity, Class<?> cls, int requestCode, Bundle data) {
        startActivityForResult(activity, cls, requestCode, null, data);
    }

    public static void startActivityForResult(Activity activity, Class<?> cls, int requestCode, String action, Bundle data) {
        Intent intent = new Intent(activity, cls);
        if (action != null) {
            intent.setAction(action);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> cls, int requestCode) {
        startActivityForResult(fragment, cls, requestCode, null, null);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> cls, int requestCode, Bundle data) {
        startActivityForResult(fragment, cls, requestCode, null, data);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> cls, int requestCode, String action, Bundle data) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        if (action != null) {
            intent.setAction(action);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        fragment.startActivityForResult(intent, requestCode);
    }
}

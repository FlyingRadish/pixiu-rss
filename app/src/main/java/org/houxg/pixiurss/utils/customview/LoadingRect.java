package org.houxg.pixiurss.utils.customview;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.houxg.pixiurss.R;

/**
 * desc:带loading wheel的提示框
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/15
 */
public class LoadingRect {

    View view;
    LoadingWheel wheel;
    TextView text;
    PopupWindow popupWindow;

    public LoadingRect(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_rect_loading, parent, false);
        wheel = (LoadingWheel) view.findViewById(R.id.wheel);
        text = (TextView) view.findViewById(R.id.text);
        text.setVisibility(View.GONE);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Drawable bg = parent.getContext().getResources().getDrawable(R.drawable.shape_dialog_bg);
        if (bg != null) {
            bg.setBounds(0, 0, bg.getMinimumWidth(), bg.getMinimumHeight());
        }
        popupWindow.setBackgroundDrawable(bg);
        popupWindow.setTouchable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.update();
    }

    public void setMessage(CharSequence msg) {
        text.setVisibility(View.VISIBLE);
        text.setText(msg);
    }

    public void setNoMessage() {
        text.setVisibility(View.GONE);
    }

    public TextView getTextView() {
        return text;
    }

    public void startLoading(View anchor) {
        if (!popupWindow.isShowing()) {
            wheel.start();
            try {
                popupWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
            } catch (Exception ex) {
                Log.w("houxg", Log.getStackTraceString(ex.getCause()));
            }
        }
    }

    public void stopLoading() {
        if (popupWindow.isShowing()) {
            wheel.stop();
            try {
                popupWindow.dismiss();
            } catch (Exception ex) {
                Log.w("houxg", Log.getStackTraceString(ex.getCause()));
            }
        }
    }

    public void destroy() {
        popupWindow.dismiss();
    }
}

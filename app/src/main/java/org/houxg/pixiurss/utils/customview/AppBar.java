package org.houxg.pixiurss.utils.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.houxg.pixiurss.R;

import java.util.List;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/20
 */
public class AppBar extends RelativeLayout {

    ImageView imgNavigator;
    TextView textTitle;
    LinearLayout actionPanel;

    public AppBar(Context context) {
        super(context);
        init(context);
    }

    public AppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.app_bar, this, true);
        imgNavigator = (ImageView) findViewById(R.id.navigator);
        textTitle = (TextView) findViewById(R.id.title);
        actionPanel = (LinearLayout) findViewById(R.id.actions);

        imgNavigator.setVisibility(GONE);
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams navigatorParams = imgNavigator.getLayoutParams();
                navigatorParams.width = getLayoutParams().height;
                imgNavigator.setLayoutParams(navigatorParams);
            }
        });
    }

    public void setHeight(float height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = (int) height;
        setLayoutParams(params);
    }

    public void setTitle(CharSequence text) {
        textTitle.setText(text);
    }

    public void setTitleTextColor(int color) {
        textTitle.setTextColor(color);
    }

    public void setNavigatorResource(@DrawableRes int resId) {
        imgNavigator.setVisibility(VISIBLE);
        imgNavigator.setImageResource(resId);
    }

    public void setNavigatorOnClickListener(OnClickListener listener) {
        imgNavigator.setOnClickListener(listener);
    }

    public TextView createTextAction(int id, CharSequence text) {
        TextView textView = new TextView(getContext());
        ViewGroup.LayoutParams actionParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(actionParams);
        textView.setId(id);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(10, 0, 10, 0);
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    public ImageView crateImageAction(@DrawableRes int resId, int height, int width) {
        ImageView action = new ImageView(getContext());
        ViewGroup.LayoutParams actionParams = new ViewGroup.LayoutParams(width, height);
        action.setLayoutParams(actionParams);
        action.setScaleType(ImageView.ScaleType.CENTER_CROP);
        action.setImageResource(resId);
        return action;
    }

    public void addAction(View action) {
        ViewGroup.LayoutParams params = actionPanel.getLayoutParams();
        actionPanel.addView(action, 0);
    }

    public void setActions(List<View> actions) {
        actionPanel.removeAllViews();
        for (View action : actions) {

            actionPanel.addView(action, 0);
        }
    }
}

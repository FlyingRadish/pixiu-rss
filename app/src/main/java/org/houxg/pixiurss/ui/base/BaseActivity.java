package org.houxg.pixiurss.ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.utils.customview.AppBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:通用Activity，加入一些快捷方法
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/8
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @Bind(R.id.app_bar)
    AppBar toolbar;

    protected void initView() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        ButterKnife.bind(this);
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle(getTitle());
            List<View> actions = getActions();
            if (actions != null) {
                toolbar.setActions(actions);
            }
            if (hasBackButton()) {
                toolbar.setNavigatorResource(R.drawable.icon_back);
                toolbar.setNavigatorOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected AppBar getAppBar() {
        return toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (toolbar != null) {
            toolbar.setTitle(getText(titleId));
        }
    }

    protected boolean hasBackButton() {
        return true;
    }

    protected List<View> getActions() {
        return null;
    }

    abstract protected int getLayoutId();

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(int msgId) {
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }
}

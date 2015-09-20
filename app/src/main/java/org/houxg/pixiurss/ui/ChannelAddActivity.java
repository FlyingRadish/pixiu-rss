package org.houxg.pixiurss.ui;

import android.view.View;
import android.widget.EditText;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.Source;
import org.houxg.pixiurss.SourceDao;
import org.houxg.pixiurss.module.App;
import org.houxg.pixiurss.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * desc:添加源
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/14
 */
public class ChannelAddActivity extends BaseActivity {

    @Bind(R.id.edit_alias)
    EditText editAlias;

    @Bind(R.id.edit_link)
    EditText editLink;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_add;
    }

    @OnClick(R.id.btn_save)
    void save(View v) {
        String alias = editAlias.getText().toString();
        String link = editLink.getText().toString();

        SourceDao channelDao = App.getDaoSession().getSourceDao();
        Source channel = new Source();
        channel.setAlias(alias);
        channel.setLink(link);

        try {
            channelDao.insert(channel);
        } catch (Exception ex) {
            ex.printStackTrace();
            toast("出错了");
            return;
        }

        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.btn_test)
    void test(View v) {
        String link = editLink.getText().toString();

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}

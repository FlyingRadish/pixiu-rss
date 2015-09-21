package org.houxg.pixiurss.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.Source;
import org.houxg.pixiurss.SourceDao;
import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.model.RSS2Item;
import org.houxg.pixiurss.module.App;
import org.houxg.pixiurss.module.adapter.RSSAdapter;
import org.houxg.pixiurss.module.rss.RSSGetter;
import org.houxg.pixiurss.ui.base.BaseActivity;
import org.houxg.pixiurss.utils.recyclerview.LinearItemDecoration;

import java.util.List;

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

    @Bind(R.id.list_article)
    RecyclerView listArticle;

    RSSGetter rssGetter;
    RSSAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_add;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listArticle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LinearItemDecoration itemDecoration = new LinearItemDecoration(12, 12, 12, 12);
        itemDecoration.setDividerSize(18);
        listArticle.addItemDecoration(itemDecoration);
        adapter = new RSSAdapter(null);
        listArticle.setAdapter(adapter);
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
        if (TextUtils.isEmpty(link)) {
            toast("地址为空会获取到外星信息的");
            return;
        }
        adapter.removeAll();
        adapter.notifyDataSetChanged();

        if (rssGetter != null) {
            rssGetter.cancel();
        }
        rssGetter = new RSSGetter(new String[]{link}, null);
        rssGetter.subscribe(listener, errorListener);
        App.ThreadPoolExecut(rssGetter);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    RSSGetter.Listener listener = new RSSGetter.Listener() {
        @Override
        public void onSuccess(String url, RSS2Channel data, List<RSS2Item> itemList, int index, int total) {
            adapter.insertOrReplace(data, itemList);
            adapter.notifyDataSetChanged();
        }
    };

    RSSGetter.ErrorListener errorListener = new RSSGetter.ErrorListener() {
        @Override
        public void onError(int index, int total) {
            toast("哦哦咦，出错了");
        }
    };
}

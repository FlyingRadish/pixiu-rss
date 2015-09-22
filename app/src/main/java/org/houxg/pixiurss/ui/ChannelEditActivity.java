package org.houxg.pixiurss.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.houxg.pixiurss.R;
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
public class ChannelEditActivity extends BaseActivity {

    private static final String BUNDLE_LINK = "link";
    private static final int TYPE_ADD = 1;
    private static final int TYPE_EDIT = 2;

    private int type = TYPE_ADD;

    @Bind(R.id.edit_alias)
    EditText editAlias;

    @Bind(R.id.edit_link)
    EditText editLink;

    @Bind(R.id.list_article)
    RecyclerView listArticle;

    RSSGetter rssGetter;
    RSSAdapter adapter;

    RSS2Channel channel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_add;
    }

    public static Bundle getOptOpenBundle(String link) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LINK, link);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String link = getIntent().getStringExtra(BUNDLE_LINK);
        channel = new RSS2Channel();
        if (!TextUtils.isEmpty(link)) {
            List<RSS2Channel> channels = RSS2Channel.fromDaos(App
                    .getDaoSession()
                    .getSourceDao()
                    .queryBuilder()
                    .where(SourceDao.Properties.Link.eq(link)).list());
            if (channels.size() > 0) {
                channel = channels.get(0);
                editAlias.setText(channel.getAlias());
                editLink.setText(channel.getLink());
                type = TYPE_EDIT;
            }
        }
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

        channel.setAlias(alias);
        channel.setLink(link);

        try {
            if (type == TYPE_ADD) {
                App.getDaoSession().getSourceDao().insert(channel.toDao());
            } else {
                App.getDaoSession().getSourceDao().update(channel.toDao());
            }
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

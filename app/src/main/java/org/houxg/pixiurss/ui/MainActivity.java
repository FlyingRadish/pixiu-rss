package org.houxg.pixiurss.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import org.houxg.pixiurss.utils.toolbox.OpenTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

public class MainActivity extends BaseActivity {

    @Bind(R.id.list_item)
    RecyclerView listItem;

    @Bind(R.id.ptr_layout)
    PtrFrameLayout ptrLayout;

    RSSGetter getter;
    RSSAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected List<View> getActions() {
        List<View> actions = new ArrayList<>();
        TextView addChannel = getAppBar().createTextAction(R.id.btn_manage_channel, "ch");
        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked(v);
            }
        });
        actions.add(addChannel);
        return actions;
    }

    @Override
    protected boolean hasBackButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearItemDecoration itemDecoration = new LinearItemDecoration(12, 12, 12, 12);
        itemDecoration.setDividerSize(18);
        listItem.setLayoutManager(layout);
        listItem.addItemDecoration(itemDecoration);
        adapter = new RSSAdapter(null);
        adapter.setClickedListener(articleClickedListener);
        listItem.setAdapter(adapter);

        MaterialHeader header = new MaterialHeader(this);
        ptrLayout.setHeaderView(header);
        ptrLayout.setPinContent(true);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.i("houxg", "start loading");
                List<Source> sources = App.getDaoSession().getSourceDao().loadAll();
                String[] channelLinks = new String[sources.size()];
                for (int i = 0; i < channelLinks.length; i++) {
                    Source source = sources.get(i);
                    channelLinks[i] = source.getLink();
                }
                if (channelLinks.length <= 0) {
                    Source source = new Source();
                    source.setAlias("cnBeta");
                    source.setLink("http://rss.cnbeta.com/rss");
                    App.getDaoSession().getSourceDao().insert(source);
                    channelLinks = new String[]{"http://rss.cnbeta.com/rss"};
                }
                getter = new RSSGetter(channelLinks, null);
                getter.subscribe(listener, errorListener);
                App.ThreadPoolExecut(getter);
            }
        });

        List<RSS2Channel> sources = RSS2Channel.fromDaos(App.getDaoSession().getSourceDao().loadAll());
        for (RSS2Channel channel : sources) {
            int pos = adapter.getItemCount();
            channel.toDao().resetArticleList();
            items.addAll(RSS2Item.fromDaos(channel.toDao().getArticleList()));
            adapter.notifyItemInserted(pos);
        }
    }

    void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_manage_channel:
                OpenTool.startActivity(this, ChannelManagerActivity.class);
                break;
        }
    }

    RSSAdapter.OnArticleClickedListener articleClickedListener = new RSSAdapter.OnArticleClickedListener() {
        @Override
        public void clickArticle(RSS2Item item) {
            Bundle bundle = ArticleWebActivity.getOpenBundle(item.getLink());
            OpenTool.startActivity(MainActivity.this, ArticleWebActivity.class, bundle);
        }
    };
    RSSGetter.ErrorListener errorListener = new RSSGetter.ErrorListener() {
        @Override
        public void onError(int index, int total) {
            Log.i("houxg", "error on=" + index);
            ptrLayout.refreshComplete();
        }
    };
    List<RSS2Item> items = new ArrayList<>();
    RSSGetter.Listener listener = new RSSGetter.Listener() {
        @Override
        public void onSuccess(String url, RSS2Channel data, List<RSS2Item> itemList, int index, int total) {
            int pos = adapter.getItemCount();
            adapter.insert(data, itemList);
            adapter.notifyItemInserted(pos);
            Log.i("houxg", "update!" + index);
            List<RSS2Channel> channels = RSS2Channel.fromDaos(App.getDaoSession()
                    .getSourceDao()
                    .queryBuilder()
                    .where(SourceDao.Properties.Link.eq(url))
                    .list());
            if (channels.size() > 0) {
                RSS2Channel channel = channels.get(0);
                for (RSS2Item item : itemList) {
                    item.toDao().setSourceId(channel.toDao().getId());
                }
                App.getDaoSession().getArticleDao().insertOrReplaceInTx(RSS2Item.toDaos(itemList));
            }
            if (index >= total - 1) {
                ptrLayout.refreshComplete();
            }
        }
    };
}

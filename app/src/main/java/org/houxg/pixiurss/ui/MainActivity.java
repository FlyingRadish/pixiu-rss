package org.houxg.pixiurss.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import org.houxg.pixiurss.utils.recyclerview.RecyclerItemClickListener;
import org.houxg.pixiurss.utils.toolbox.OpenTool;
import org.joda.time.DateTime;

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

    long time = 0;

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
        listItem.setItemAnimator(new DefaultItemAnimator());
        listItem.addOnItemTouchListener(new RecyclerItemClickListener(this).setItemClickListener(itemClickListener));

        adapter = new RSSAdapter(null);
//        adapter.setArticleClickedListener(articleClickedListener);
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
                time = System.currentTimeMillis();
//                Log.i("houxg", "start");
                App.ThreadPoolExecut(getter);
            }
        });

        List<RSS2Channel> sources = RSS2Channel.fromDaos(App.getDaoSession().getSourceDao().loadAll());
        for (RSS2Channel channel : sources) {
            int pos = adapter.getItemCount();
            channel.toDao().resetArticleList();
            updateData(channel, RSS2Item.fromDaos(channel.toDao().getArticleList()));
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

    @Override
    public void onBackPressed() {
        if (getter != null && getter.isRunning()) {
            ptrLayout.refreshComplete();
            getter.cancel();
        } else {
            super.onBackPressed();
        }
    }


    private void updateData(RSS2Channel channel, List<RSS2Item> itemList) {
//        Log.i("houxg", "UI change start, saving " + showTime());
        DateTime dateTime = new DateTime().withMillisOfDay(0);
        long today = dateTime.getMillis();
//        Log.i("houxg", "today=" + dateTime.toString("yyyy-MM-dd HH:mm:ss") + ", mills=" + today);
        int endPos = -1;
        for (int i = 0; i < itemList.size(); i++) {
            RSS2Item item = itemList.get(i);
            if (item.getPubDate() >= today) {
                endPos = i;
            }
        }
        if (endPos > 0) {
            itemList = itemList.subList(0, endPos);
        } else {
            itemList.clear();
        }
//        Log.i("houxg", "UI data pre-handle, " + showTime());
        int pos = adapter.getItemCount();
        adapter.insertOrReplace(channel, itemList);
        //        Log.i("houxg", "UI data updated, " + showTime());
    }

    String showTime() {
        long now = System.currentTimeMillis();
        String dt = "dt=" + (now - time);
        time = now;
        return dt;
    }

    //    RSSAdapter.OnArticleClickedListener articleClickedListener = new RSSAdapter.OnArticleClickedListener() {
//        @Override
//        public void clickArticle(RSS2Item item) {
//            Bundle bundle = ArticleWebActivity.getOpenBundle(item.getLink());
//            OpenTool.startActivity(MainActivity.this, ArticleWebActivity.class, bundle);
//        }
//    };
    RSSGetter.ErrorListener errorListener = new RSSGetter.ErrorListener() {
        @Override
        public void onError(int index, int total) {
            Log.i("houxg", "error on=" + index);
            ptrLayout.refreshComplete();
        }
    };
    RSSGetter.Listener listener = new RSSGetter.Listener() {
        @Override
        public void onSuccess(String url, RSS2Channel data, List<RSS2Item> itemList, int index, int total) {
//            Log.i("houxg", "Network data come, network " + showTime());
            List<RSS2Channel> channels = RSS2Channel.fromDaos(App.getDaoSession()
                    .getSourceDao()
                    .queryBuilder()
                    .where(SourceDao.Properties.Link.eq(url))
                    .list());
            if (channels.size() > 0) {
                RSS2Channel channel = channels.get(0);
                channel.setTitle(data.getTitle());
                for (int i = 0; i < itemList.size(); i++) {
                    RSS2Item item = itemList.get(i);
                    item.toDao().setSourceId(channel.toDao().getId());
                }
                App.getDaoSession().getArticleDao().insertOrReplaceInTx(RSS2Item.toDaos(itemList));
                App.getDaoSession().getSourceDao().update(channel.toDao());

                updateData(channel, itemList);
            }

            if (index >= total - 1) {
                ptrLayout.refreshComplete();
            }
        }
    };
    RecyclerItemClickListener.OnItemClickListener itemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView recyclerView, View view, int position) {
            Object obj = adapter.getItem(position);
            if (obj instanceof RSS2Item) {
                RSS2Item item = (RSS2Item) obj;
                Bundle bundle = ArticleWebActivity.getOpenBundle(item.getLink());
                OpenTool.startActivity(MainActivity.this, ArticleWebActivity.class, bundle);
            } else {
                Log.wtf("houxg", "not a item");
            }
        }
    };
}

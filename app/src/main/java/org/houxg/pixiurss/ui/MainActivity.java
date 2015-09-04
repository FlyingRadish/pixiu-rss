package org.houxg.pixiurss.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.model.RSS2Item;
import org.houxg.pixiurss.module.adapter.ArticleAdapter;
import org.houxg.pixiurss.module.rss.RSSGetter;
import org.houxg.pixiurss.utils.recyclerview.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.title_activity)
    Toolbar toolbar;

    @Bind(R.id.list_item)
    RecyclerView listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.icon_back);

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearItemDecoration itemDecoration = new LinearItemDecoration(12, 12, 12, 12);
        itemDecoration.setDividerSize(18);
        listItem.setLayoutManager(layout);
        listItem.addItemDecoration(itemDecoration);
        adapter = new ArticleAdapter(items);
        listItem.setAdapter(adapter);

        RSSGetter getter = new RSSGetter(new String[]{"http://rss.cnbeta.com/rss", "http://jandan.net/feed"}, null);
        getter.subscribe(listener, errorListener);
        new Thread(getter).start();
    }

    ArticleAdapter adapter;

    RSSGetter.Listener listener = new RSSGetter.Listener() {
        @Override
        public void onSuccess(RSS2Channel data, int index, int total) {
            int pos = items.size();
            items.addAll(data.getItems());
            adapter.notifyItemInserted(pos);
            Log.i("houxg", "update!" + index);
        }
    };

    RSSGetter.ErrorListener errorListener = new RSSGetter.ErrorListener() {
        @Override
        public void onError(int index, int total) {
            Log.i("houxg", "error on=" + index);
        }
    };

    List<RSS2Item> items = new ArrayList<>();

}

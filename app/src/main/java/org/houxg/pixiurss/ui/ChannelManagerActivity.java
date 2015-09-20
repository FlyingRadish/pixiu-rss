package org.houxg.pixiurss.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.SourceDao;
import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.module.App;
import org.houxg.pixiurss.ui.base.BaseActivity;
import org.houxg.pixiurss.utils.customview.LoadingRect;
import org.houxg.pixiurss.utils.recyclerview.LinearItemDecoration;
import org.houxg.pixiurss.utils.recyclerview.RecyclerListAdapter;
import org.houxg.pixiurss.utils.toolbox.OpenTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:源管理页面
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/13
 */
public class ChannelManagerActivity extends BaseActivity {

    private static final int REQ_ADD = 1;

    @Bind(R.id.list_channel)
    RecyclerView listChannel;

    @Bind(R.id.activity)
    ViewGroup rootView;

    ChannelAdapter adapter;
    LoadingRect loadingView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_manager;
    }

    @Override
    protected List<View> getActions() {
        List<View> actions = new ArrayList<>();
        TextView addChannel = getAppBar().createTextAction(R.id.btn_add_channel, "ch");
        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked(v);
            }
        });
        actions.add(addChannel);
        return actions;
    }

    void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_add_channel:
                OpenTool.startActivityForResult(this, ChannelAddActivity.class, REQ_ADD);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listChannel.setLayoutManager(new LinearLayoutManager(this));
        listChannel.addItemDecoration(new LinearItemDecoration(0, 0, 0, 0).setDividerSize(12));


        SourceDao channelDao = App.getDaoSession().getSourceDao();

        channelDao.loadAll();
        adapter = new ChannelAdapter(RSS2Channel.fromDaos(channelDao.loadAll()));
        listChannel.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD && resultCode == RESULT_OK) {
            adapter.changeDatasources(RSS2Channel.fromDaos(App.getDaoSession().getSourceDao().loadAll()));
        }
    }

    static class ChannelAdapter extends RecyclerListAdapter<RSS2Channel, ChannelAdapter.ViewHolder> {

        public ChannelAdapter(List<RSS2Channel> data) {
            super(R.layout.item_channel, data);
        }

        @Override
        public void onBind(ViewHolder holder, RSS2Channel item, int position) {
            holder.textAlias.setText(item.getTitle());
            holder.textLink.setText(item.getLink());
        }

        @Override
        protected ViewHolder onBindViewToVH(View view, int type) {
            return new ViewHolder(view);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.text_alias)
            TextView textAlias;
            @Bind(R.id.text_link)
            TextView textLink;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}

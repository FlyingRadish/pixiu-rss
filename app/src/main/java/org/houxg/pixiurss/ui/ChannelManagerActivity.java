package org.houxg.pixiurss.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.SourceDao;
import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.module.App;
import org.houxg.pixiurss.ui.base.BaseActivity;
import org.houxg.pixiurss.utils.CancelRunnable;
import org.houxg.pixiurss.utils.customview.LoadingRect;
import org.houxg.pixiurss.utils.recyclerview.LinearItemDecoration;
import org.houxg.pixiurss.utils.recyclerview.RecyclerItemClickListener;
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
    private static final int TIMEOUT_DELETE = 5000;

    @Bind(R.id.list_channel)
    RecyclerView listChannel;

    @Bind(R.id.activity)
    ViewGroup rootView;

    ChannelAdapter adapter;
    LoadingRect loadingView;
    DeleteWorker deleteWorker;

    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, final int direction) {
            Log.i("houxg", "swipe");
            int pos = viewHolder.getAdapterPosition();
            RSS2Channel channel = adapter.getItem(pos);
            adapter.remove(pos);
            adapter.notifyItemRemoved(pos);

            deleteWorker = new DeleteWorker(channel, pos);
            new Thread(deleteWorker).start();
            Snackbar snackbar = Snackbar.make(rootView, "删除了" + channel.getAlias(), TIMEOUT_DELETE - 1000);
            snackbar.setAction("撤销", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteWorker != null) {
                        RSS2Channel ch = deleteWorker.waitToDelete;
                        int pos = deleteWorker.pos;
                        deleteWorker.cancel();
                        adapter.insert(pos, ch);
                        adapter.notifyItemInserted(pos);
                    }
                }
            });
            snackbar.show();
        }
    });

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listChannel.setLayoutManager(new LinearLayoutManager(this));
        listChannel.addItemDecoration(new LinearItemDecoration(0, 0, 0, 0).setDividerSize(12));
        listChannel.setItemAnimator(new DefaultItemAnimator());
        helper.attachToRecyclerView(listChannel);
        listChannel.addOnItemTouchListener(new RecyclerItemClickListener(this)
                .setItemClickListener(itemClickListener));

        SourceDao channelDao = App.getDaoSession().getSourceDao();
        channelDao.loadAll();
        adapter = new ChannelAdapter(RSS2Channel.fromDaos(channelDao.loadAll()));
        listChannel.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD && resultCode == RESULT_OK) {
            adapter.changeDataSource(RSS2Channel.fromDaos(App.getDaoSession().getSourceDao().loadAll()));
            adapter.notifyDataSetChanged();
        }
    }


    void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_add_channel:
                OpenTool.startActivityForResult(this, ChannelEditActivity.class, REQ_ADD);
                break;
        }
    }

    RecyclerItemClickListener.OnItemClickListener itemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView recyclerView, View view, int position) {
            RSS2Channel channel = adapter.getItem(position);
            Bundle bundle = ChannelEditActivity.getOptOpenBundle(channel.getLink());
            OpenTool.startActivityForResult(ChannelManagerActivity.this, ChannelEditActivity.class, REQ_ADD, bundle);
        }
    };

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

        public void remove(int pos) {
            data.remove(pos);
        }

        public void insert(int pos, RSS2Channel channel) {
            data.add(pos, channel);
        }
    }

    class DeleteWorker implements CancelRunnable {

        RSS2Channel waitToDelete;
        int pos;

        public DeleteWorker(RSS2Channel waitToDelete, int pos) {
            this.waitToDelete = waitToDelete;
            this.pos = pos;
        }

        @Override
        public void cancel() {
            waitToDelete = null;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(TIMEOUT_DELETE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (waitToDelete != null) {
                try {
                    Log.i("houxg", "delete");
                    App.getDaoSession().getSourceDao().delete(waitToDelete.toDao());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

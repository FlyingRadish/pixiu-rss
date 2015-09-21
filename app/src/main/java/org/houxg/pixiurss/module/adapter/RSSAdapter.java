package org.houxg.pixiurss.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.model.RSS2Item;
import org.houxg.pixiurss.utils.recyclerview.RecyclerListAdapter;
import org.houxg.pixiurss.utils.toolbox.ListTool;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * desc:RSS2 Item adapter
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/3
 */
public class RSSAdapter extends RecyclerListAdapter<Object, RecyclerView.ViewHolder> {

    private static final int TYPE_CHANNEL = 1;
    private static final int TYPE_ITEM = 2;

    public interface OnArticleClickedListener {
        void clickArticle(RSS2Item item);
    }

    OnArticleClickedListener clickedListener;

    public RSSAdapter(List<Object> data) {
        super(R.layout.item_rssitem, data);
        channelWrappers = new ArrayList<>();
    }

    @Override
    protected int getLayoutIdByType(int type) {
        if (type == TYPE_CHANNEL) {
            return R.layout.item_rsschannel;
        } else {
            return R.layout.item_rssitem;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItem(position);
        if (obj instanceof ChannelWrapper) {
            return TYPE_CHANNEL;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, Object obj, int position) {
        if (obj instanceof ChannelWrapper) {
            ChannelWrapper channel = (ChannelWrapper) obj;
            ChannelViewHolder channelViewHolder = (ChannelViewHolder) holder;
            channelViewHolder.textAlias.setText(channel.wrapper.getAlias());
        }
        if (obj instanceof RSS2Item) {
            RSS2Item item = (RSS2Item) obj;
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.textTitle.setText(Html.fromHtml(item.getTitle()));
            itemViewHolder.textContent.setText(Html.fromHtml(item.getDesc()));
            itemViewHolder.textSrc.setText("from " + item.getChannelTitle());
            DateTime time = new DateTime(item.getPubDate());
            itemViewHolder.textTime.setText(time.toString("MM-dd H:mm:ss"));
            holder.itemView.setTag(item);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onBindViewToVH(View view, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_CHANNEL) {
            holder = new ChannelViewHolder(view);
        } else {
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RSS2Item item = (RSS2Item) v.getTag();
                    if (clickedListener != null) {
                        clickedListener.clickArticle(item);
                    }
                }
            });
            holder = itemViewHolder;
        }
        return holder;
    }

    List<ChannelWrapper> channelWrappers;

    public void insertOrReplace(RSS2Channel channel, List<RSS2Item> items) {
        if (data == null) {
            data = new ArrayList<>();
        }
        ChannelWrapper wrapper = new ChannelWrapper();
        wrapper.wrapper = channel;
        int pos = data.indexOf(wrapper);
        if (pos < 0) {
            wrapper = new ChannelWrapper();
            wrapper.wrapper = channel;
            wrapper.size = items.size();
            data.add(wrapper);
            data.addAll(items);
        } else {
            wrapper = (ChannelWrapper) data.get(pos);
            ListTool.mergeList(data, items, pos + 1, pos + wrapper.size);
            wrapper.size = items.size();
        }
    }

    public void removeAll() {
        data = new ArrayList<>();
    }

    public void setClickedListener(OnArticleClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_alias)
        TextView textAlias;

        public ChannelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_title)
        TextView textTitle;
        @Bind(R.id.text_content)
        TextView textContent;
        @Bind(R.id.text_src)
        TextView textSrc;
        @Bind(R.id.text_time)
        TextView textTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ChannelWrapper {
        RSS2Channel wrapper;
        int size = 0;

        @Override
        public boolean equals(Object o) {
            if (wrapper == null) {
                return false;
            }
            if (o instanceof ChannelWrapper) {
                ChannelWrapper channelWrapper = (ChannelWrapper) o;
                return wrapper.equals(channelWrapper.wrapper);
            } else if (o instanceof RSS2Channel) {
                RSS2Channel channel = (RSS2Channel) o;
                return wrapper.equals(channel);
            }
            return false;
        }
    }
}

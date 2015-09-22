package org.houxg.pixiurss.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.model.RSS2Item;
import org.houxg.pixiurss.utils.recyclerview.RecyclerListAdapter;
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
            holder = new ItemViewHolder(view);
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
        int animPos;
        if (pos < 0) {
            animPos = data.size();
            wrapper = new ChannelWrapper();
            wrapper.wrapper = channel;
            wrapper.size = items.size();
            data.add(wrapper);
            data.addAll(items);
            notifyItemRangeInserted(animPos, items.size() + 1);
        } else {
            wrapper = (ChannelWrapper) data.get(pos);
            int start = pos + 1;
            int end = pos + wrapper.size;
            int range = end - start + 1;
            boolean shouldDelete = range >= items.size();
            int index = start;
            int changeEnd = index + (shouldDelete ? items.size() : range);
            int deleteEnd = index + range;
            int itemIndex = 0;
            animPos = index;
            for (; index < changeEnd; index++) {
                data.set(index, items.get(itemIndex));
                itemIndex++;
            }
            notifyItemRangeChanged(animPos, itemIndex);
            if (shouldDelete) {
                int deleteStickIndex = index;
                animPos = index;
                for (; index < deleteEnd; index++) {
                    data.remove(deleteStickIndex);
                }
                notifyItemRangeRemoved(animPos, deleteEnd - animPos - 1);
            } else {
                List<RSS2Item> addItems = items.subList(itemIndex, items.size());
                data.addAll(index, addItems);
                notifyItemRangeInserted(index, addItems.size());
            }
            wrapper.size = items.size();
        }
    }

    public void removeAll() {
        int size = data.size();
        data = new ArrayList<>();
        notifyItemRangeRemoved(0, size);
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

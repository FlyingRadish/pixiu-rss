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
public class ArticleAdapter extends RecyclerListAdapter<RSS2Item, ArticleAdapter.ViewHolder> {

    public interface OnArticleClickedListener {
        void clickArticle(RSS2Item item);
    }

    OnArticleClickedListener clickedListener;

    public ArticleAdapter(List<RSS2Item> data) {
        super(R.layout.item_rssitem, data);
    }

    @Override
    public void onBind(ViewHolder holder, RSS2Item item, int position) {
        holder.textTitle.setText(Html.fromHtml(item.getTitle()));
        holder.textContent.setText(Html.fromHtml(item.getDesc()));
        holder.textSrc.setText("from " + item.getChannelTitle());
        DateTime time = new DateTime(item.getPubDate());
        holder.textTime.setText(time.toString("MM-dd H:mm:ss"));
        holder.itemView.setTag(item);
    }

    @Override
    protected ArticleAdapter.ViewHolder onBindViewToVH(View view, int type) {
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RSS2Item item = (RSS2Item) v.getTag();
                if (clickedListener != null) {
                    clickedListener.clickArticle(item);
                }
            }
        });
        return holder;
    }

    public void setClickedListener(OnArticleClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_title)
        TextView textTitle;
        @Bind(R.id.text_content)
        TextView textContent;
        @Bind(R.id.text_src)
        TextView textSrc;
        @Bind(R.id.text_time)
        TextView textTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ChannelWrapper {
        RSS2Channel wrapper;
    }
}

package org.houxg.pixiurss.module.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.model.RSS2Item;
import org.houxg.pixiurss.utils.RecyclerListAdapter;

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


    public ArticleAdapter(List<RSS2Item> data) {
        super(R.layout.item_article, data);
    }

    @Override
    public void onBind(ViewHolder holder, RSS2Item item, int position) {
        holder.textTitle.setText(item.getTitle());
        holder.textContent.setText(item.getDesc());
        holder.textSrc.setText("from " + item.getChannelTitle());
        holder.textTime.setText(item.getPubDate());
    }

    @Override
    protected ArticleAdapter.ViewHolder onBindViewToVH(View view) {
        return new ViewHolder(view);
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
}

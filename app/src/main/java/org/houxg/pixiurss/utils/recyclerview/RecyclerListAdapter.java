package org.houxg.pixiurss.utils.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * desc:Recycler通用Adapter
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/3
 */
public abstract class RecyclerListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> data;
    protected int layoutId = -1;

    public RecyclerListAdapter(int layoutId, List<T> data) {
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutIdByType(viewType), parent, false);
        VH holder = onBindViewToVH(view, viewType);
        return holder;
    }

    protected int getLayoutIdByType(int type) {
        return layoutId;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBind(holder, data.get(position), position);
    }

    public abstract void onBind(VH holder, T item, int position);

    protected abstract VH onBindViewToVH(View view, int type);

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    public T getItem(int pos) {
        if (data == null || pos < 0 || pos > data.size() - 1) {
            return null;
        }
        return data.get(pos);
    }

    public void changeDataSource(List<T> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }
}

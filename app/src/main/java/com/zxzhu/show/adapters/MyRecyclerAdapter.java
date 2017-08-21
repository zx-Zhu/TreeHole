package com.zxzhu.show.adapters;

/**
 * Created by zxzhu on 2017/8/20.
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;

import java.util.List;

public class MyRecyclerAdapter< T extends ViewDataBinding> extends RecyclerView.Adapter<MyRecyclerAdapter.BaseViewHolder> {
    private List<AVObject> list;
    private LayoutInflater inflater;
    private Context context;
    @LayoutRes
    private int layout;
    private BindView<T> bindView;

    public MyRecyclerAdapter(Context context, @LayoutRes int itemLayout, List<AVObject> list, BindView<T> bindView) {
        this.context = context;
        this.layout = itemLayout;
        this.list = list;
        this.bindView = bindView;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(layout, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindView.onBindViewHolder((T) holder.getBinding(), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface BindView<T> {
        void onBindViewHolder(T b, int position);
        void onRecycled(T b);
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        bindView.onRecycled((T) holder.getBinding());
        super.onViewRecycled(holder);
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding b;

        public BaseViewHolder(View itemView) {
            super(itemView);
            b = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding() {
            return b;
        }
    }
}

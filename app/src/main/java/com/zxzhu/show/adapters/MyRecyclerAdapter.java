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

public class MyRecyclerAdapter< T extends ViewDataBinding> extends RecyclerView.Adapter<MyRecyclerAdapter.BaseViewHolder> {
    private int size;
    private LayoutInflater inflater;
    private Context context;
    @LayoutRes
    private int layout;
    private BindView<T> bindView;
    private String str = "AAA#BBB#CCC";


    public MyRecyclerAdapter(Context context, @LayoutRes int itemLayout, int size, BindView<T> bindView) {
        this.context = context;
        this.layout = itemLayout;
        this.bindView = bindView;
        this.size = size;
        str.split(";");
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(layout, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        try {
            bindView.onBindViewHolder((T) holder.getBinding(), position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public interface BindView<T> {
        void onBindViewHolder(T b, int position) throws Exception;
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

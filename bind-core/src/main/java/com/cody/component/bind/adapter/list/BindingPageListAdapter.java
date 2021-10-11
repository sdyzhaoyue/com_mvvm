/*
 * ************************************************************
 * 文件：BindingPageListAdapter.java  模块：bind-adapter  项目：component
 * 当前修改时间：2019年04月23日 18:23:20
 * 上次修改时间：2019年04月23日 12:54:46
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：bind-adapter
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.bind.adapter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cody.component.handler.data.ItemViewDataHolder;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.cody.component.handler.data.ItemFooterOrHeaderData.HEADER_OR_FOOTER_VIEW_TYPE;

/**
 * Created by xu.yi. on 2019/3/28.
 * 分页抽象列表adapter
 */
public abstract class BindingPageListAdapter<Item extends ItemViewDataHolder> extends PagedListAdapter<Item, BindingViewHolder> implements IBindingAdapter {

    protected RecyclerView mRecyclerView;
    protected OnBindingItemClickListener mItemClickListener;//item 事件监听
    protected View.OnCreateContextMenuListener mItemLongClickListener;//item 长按事件监听
    protected final LifecycleOwner mLifecycleOwner;

    @Override
    public void setItemClickListener(OnBindingItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void setItemLongClickListener(View.OnCreateContextMenuListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    protected BindingPageListAdapter(LifecycleOwner lifecycleOwner) {
        super(new BindingItemDiffCallback<>());
        mLifecycleOwner = lifecycleOwner;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager g = (GridLayoutManager) manager;
            g.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return HEADER_OR_FOOTER_VIEW_TYPE == getItemViewType(position) ? g.getSpanCount() : 1;
                }
            });
        }

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = null;
    }

    @Override
    public Item getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        ItemViewDataHolder item = getItem(position);
        if (item != null) {
            return item.getItemType();
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayoutId(viewType), parent, false);
        binding.setLifecycleOwner(mLifecycleOwner);
        return new BindingViewHolder(binding.getRoot());
    }

    @CallSuper
    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        ItemViewDataHolder item = getItem(position);
        if (item != null) {
            holder.bindTo(item, mRecyclerView, mItemLongClickListener, mItemClickListener);
        } else {
            holder.clear();
        }
    }
}

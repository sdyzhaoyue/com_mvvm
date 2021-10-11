/*
 * ************************************************************
 * 文件：IBasePageListView.java  模块：app-core  项目：component
 * 当前修改时间：2019年07月23日 08:41:20
 * 上次修改时间：2019年06月25日 10:52:57
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：app-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.app;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cody.component.bind.adapter.list.BindingPageListAdapter;
import com.cody.component.bind.adapter.list.MultiBindingPageListAdapter;
import com.cody.component.handler.data.ItemViewDataHolder;
import com.cody.component.handler.interfaces.OnRetryListener;
import com.cody.component.handler.interfaces.Scrollable;

/**
 * Created by xu.yi. on 2019/4/10.
 * 列表绑定需要实现的接口
 */
public interface IBasePageListView extends OnRetryListener, Scrollable {
    @NonNull
    BindingPageListAdapter<ItemViewDataHolder> buildListAdapter();

    @NonNull
    BindingPageListAdapter<ItemViewDataHolder> getListAdapter();

    @NonNull
    LinearLayoutManager buildLayoutManager();

    @NonNull
    RecyclerView getRecyclerView();
}

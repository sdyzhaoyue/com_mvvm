/*
 * ************************************************************
 * 文件：CatOverviewFragment.java  模块：http-cat  项目：component
 * 当前修改时间：2019年04月23日 18:23:19
 * 上次修改时间：2019年04月13日 08:43:55
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：http-cat
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.cat.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.cody.component.app.fragment.AbsBindFragment;
import com.cody.component.cat.R;
import com.cody.component.cat.databinding.CatFragmentOverviewBinding;
import com.cody.component.cat.db.data.ItemHttpData;
import com.cody.component.handler.viewmodel.BaseViewModel;

/**
 * Created by xu.yi. on 2019/4/5.
 * CatOverviewFragment
 */
public class CatOverviewFragment extends AbsBindFragment<CatFragmentOverviewBinding, BaseViewModel, ItemHttpData> {
    private static final String ITEM_VIEW_DATA = "itemHttpData";
    private ItemHttpData mItemHttpData;

    public static CatOverviewFragment newInstance(ItemHttpData itemHttpData) {
        CatOverviewFragment fragment = new CatOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_VIEW_DATA, itemHttpData);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CatOverviewFragment() {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.cat_fragment_overview;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected ItemHttpData getViewData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mItemHttpData = bundle.getParcelable(ITEM_VIEW_DATA);
        }
        return mItemHttpData;
    }

    @NonNull
    @Override
    public Class<BaseViewModel> getVMClass() {
        return BaseViewModel.class;
    }
}
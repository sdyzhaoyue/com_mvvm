/*
 * ************************************************************
 * 文件：IBaseView.java  模块：handler-core  项目：component
 * 当前修改时间：2019年04月23日 18:23:20
 * 上次修改时间：2019年04月14日 00:14:46
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：handler-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.handler.view;

import com.cody.component.handler.viewmodel.BaseViewModel;
import com.cody.component.lib.view.IView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by xu.yi. on 2019/3/25.
 * component
 */
public interface IBaseView extends IView {
    default <VM extends BaseViewModel> VM getViewModel(@NonNull Class<VM> viewModelClass) {
        return getViewModel(viewModelClass, null);
    }

    <VM extends BaseViewModel> VM getViewModel(@NonNull Class<VM> viewModelClass, @Nullable ViewModelProvider.Factory factory);
}

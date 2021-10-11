/*
 * ************************************************************
 * 文件：IViewModel.java  模块：handler-core  项目：component
 * 当前修改时间：2019年04月23日 18:51:40
 * 上次修改时间：2019年04月23日 18:23:20
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：handler-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.handler.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.cody.component.handler.define.ViewAction;
import com.cody.component.lib.view.IView;

/**
 * Created by xu.yi. on 2019/3/25.
 * component
 */
public interface IViewModel extends IView {
    MutableLiveData<ViewAction> getActionLiveData();

    /**
     * 处理其他action，扩展用
     */
    void executeAction(ViewAction action);
    <T extends BaseViewModel> T setLifecycleOwner(LifecycleOwner lifecycleOwner);
}

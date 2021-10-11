/*
 * ************************************************************
 * 文件：ItemFooterOrHeaderData.java  模块：handler-core  项目：component
 * 当前修改时间：2019年04月24日 09:39:11
 * 上次修改时间：2019年04月24日 09:38:27
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：handler-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.handler.data;

import com.cody.component.handler.define.RequestStatus;
import com.cody.component.handler.livedata.BooleanLiveData;
import com.cody.component.handler.livedata.StringLiveData;

import java.util.Objects;


/**
 * Created by xu.yi. on 2019/4/8.
 * 列表底部数据类
 */
public class ItemFooterOrHeaderData extends ItemViewDataHolder {
    final public static int HEADER_OR_FOOTER_VIEW_TYPE = -1;
    private boolean mShowFooter = true;
    private BooleanLiveData mNoMoreItem = new BooleanLiveData(false);
    private BooleanLiveData mError = new BooleanLiveData(false);
    private BooleanLiveData mLoading = new BooleanLiveData(false);
    private StringLiveData mErrorMessage = new StringLiveData("");

    public ItemFooterOrHeaderData() {
        super(HEADER_OR_FOOTER_VIEW_TYPE);
    }

    public boolean isShowFooter() {
        return mShowFooter;
    }

    public void setShowFooter(final boolean showFooter) {
        mShowFooter = showFooter;
    }

    public BooleanLiveData getNoMoreItem() {
        return mNoMoreItem;
    }

    public BooleanLiveData getError() {
        return mError;
    }

    public BooleanLiveData getLoading() {
        return mLoading;
    }

    public StringLiveData getErrorMessage() {
        return mErrorMessage;
    }

    public void setRequestStatus(final RequestStatus status) {
        if (status.isRefreshing()) return;
        mNoMoreItem.postValue(status.isEnd());
        mError.postValue(status.isError());
        mLoading.postValue(status.isLoadingBefore() || status.isLoadingAfter());
        mErrorMessage.postValue(status.getMessage());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ItemFooterOrHeaderData that = (ItemFooterOrHeaderData) o;
        return mShowFooter == that.mShowFooter &&
                Objects.equals(mNoMoreItem, that.mNoMoreItem) &&
                Objects.equals(mError, that.mError) &&
                Objects.equals(mLoading, that.mLoading) &&
                Objects.equals(mErrorMessage, that.mErrorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mShowFooter, mNoMoreItem, mError, mLoading, mErrorMessage);
    }
}
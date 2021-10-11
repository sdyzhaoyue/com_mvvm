/*
 * ************************************************************
 * 文件：CatViewModel.java  模块：http-cat  项目：component
 * 当前修改时间：2019年04月23日 18:23:19
 * 上次修改时间：2019年04月23日 11:55:12
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：http-cat
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.cat.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.cody.component.cat.HttpCat;
import com.cody.component.cat.db.HttpCatDao;
import com.cody.component.cat.db.HttpCatDatabase;
import com.cody.component.cat.db.data.ItemHttpData;
import com.cody.component.cat.notification.NotificationManagement;
import com.cody.component.handler.RequestStatusUtil;
import com.cody.component.handler.data.FriendlyViewData;
import com.cody.component.handler.data.ItemViewDataHolder;
import com.cody.component.handler.define.RequestStatus;
import com.cody.component.handler.viewmodel.AbsPageListViewModel;
import com.cody.component.handler.viewmodel.BaseViewModel;
import com.cody.component.util.RecyclerViewUtil;



/**
 * Created by xu.yi. on 2019/3/31.
 * CatViewModel
 */
public class CatViewModel extends AbsPageListViewModel<FriendlyViewData, Integer> {
    private HttpCatDao mHttpCatDao;

    private LiveData<ItemHttpData> mRecordLiveData = new MutableLiveData<>();

    public CatViewModel() {
        super(new FriendlyViewData());
    }

    @Override
    protected DataSource.Factory<Integer, ItemViewDataHolder> createDataSourceFactory() {
        mHttpCatDao = HttpCatDatabase
                .getInstance()
                .getHttpInformationDao();
        return mHttpCatDao.getDataSource().map(input -> input);
    }

    @Override
    public <T extends BaseViewModel> T setLifecycleOwner(final LifecycleOwner lifecycleOwner) {
        if (mLifecycleOwner == null && lifecycleOwner != null) {
            mHttpCatDao.count().observe(lifecycleOwner, count -> submitStatus(count > 0 ? getRequestStatus().end() : getRequestStatus().empty()));
        }
        return super.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected void startOperation(final RequestStatus requestStatus) {
        super.startOperation(requestStatus);
        new Handler(Looper.getMainLooper()).postDelayed(() -> submitStatus(RequestStatusUtil.getRequestStatus(requestStatus, getPagedList().getValue())),500);
    }

    public void clearAllCache() {
        new Thread(() -> {
            mHttpCatDao.deleteAll();
            submitStatus(getRequestStatus().empty());
        }).start();
    }

    public void clearNotification() {
        NotificationManagement.getInstance(HttpCat.getInstance().getContext()).dismiss();
    }

    public void queryRecordById(long id) {
        mRecordLiveData = mHttpCatDao.queryRecordObservable(id);
    }

    public LiveData<ItemHttpData> getRecordLiveData() {
        return mRecordLiveData;
    }
}

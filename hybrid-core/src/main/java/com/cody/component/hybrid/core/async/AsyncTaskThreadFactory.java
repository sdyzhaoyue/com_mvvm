/*
 * ************************************************************
 * 文件：AsyncTaskThreadFactory.java  模块：hybrid-core  项目：component
 * 当前修改时间：2019年04月23日 18:23:20
 * 上次修改时间：2019年04月13日 08:44:03
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：hybrid-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.hybrid.core.async;

import android.os.Process;
import android.util.Log;

import com.cody.component.util.LogUtil;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Cody.yi on 17/4/12.
 * AsyncTaskThreadFactory
 */
public class AsyncTaskThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(final Runnable runnable) {
        Runnable wrapper = new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                } catch (Throwable t) {
                    LogUtil.e(Log.getStackTraceString(t));
                }
                runnable.run();
            }
        };
        Thread thread = new Thread(wrapper, "JsBridge AsyncTaskExecutor");
        if (thread.isDaemon())
            thread.setDaemon(false);
        return thread;
    }
}

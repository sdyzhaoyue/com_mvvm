/*
 * ************************************************************
 * 文件：LauncherUtil.java  模块：http-cat  项目：component
 * 当前修改时间：2019年04月23日 18:23:19
 * 上次修改时间：2019年04月13日 08:43:54
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：http-cat
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.cat.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.cody.component.app.local.Repository;
import com.cody.component.util.LogUtil;

/**
 * Created by xu.yi. on 2019/4/5.
 * component
 */
public class LauncherUtil {
    public static String VISIBLE = "http_cat_visible";

    public static boolean isCatVisible() {
        return Repository.getLocalBoolean(VISIBLE);
    }

    public static void launcherVisible(final Context context, final Class launcher) {
        launcherVisible(context, launcher, isCatVisible());
    }

    /**
     * 显示隐藏App图标
     * @param context c
     * @param launcher l
     * @param visible v
     */
    static public void launcherVisible(Context context, Class launcher, boolean visible) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, launcher);
        int res = getLauncherStats(packageManager, componentName);
        if (visible == (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED)) return;
        if (visible) {
            showLauncher(packageManager, componentName);
        } else {
            hideLauncher(packageManager, componentName);
        }
        Repository.setLocalBoolean(VISIBLE, visible);
    }

    public static void hideLauncher(PackageManager packageManager, ComponentName componentName) {
        try {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            LogUtil.e(Log.getStackTraceString(e));
        }
    }

    public static void showLauncher(PackageManager packageManager, ComponentName componentName) {
        try {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            LogUtil.e(Log.getStackTraceString(e));
        }
    }

    public static int getLauncherStats(PackageManager packageManager, ComponentName componentName) {
        try {
            return packageManager.getComponentEnabledSetting(componentName);
        } catch (Exception e) {
            LogUtil.e(Log.getStackTraceString(e));
        }
        return PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }
}

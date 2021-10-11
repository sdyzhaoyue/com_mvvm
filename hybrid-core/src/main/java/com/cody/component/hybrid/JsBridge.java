/*
 * ************************************************************
 * 文件：JsBridge.java  模块：hybrid-core  项目：component
 * 当前修改时间：2019年04月23日 18:23:20
 * 上次修改时间：2019年04月13日 08:44:03
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：hybrid-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.hybrid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.cody.component.hybrid.core.JsCallback;
import com.cody.component.hybrid.core.JsHandler;
import com.cody.component.hybrid.core.JsHandlerFactory;
import com.cody.component.hybrid.core.JsInteract;
import com.cody.component.hybrid.core.JsLifeCycle;
import com.cody.component.hybrid.core.JsWebChromeClient;
import com.cody.component.hybrid.core.JsWebViewClient;
import com.cody.component.hybrid.data.HtmlConfig;
import com.cody.component.hybrid.data.HtmlViewData;
import com.cody.component.util.ActivityUtil;
import com.cody.component.util.LogUtil;

import java.lang.reflect.Method;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Cody.yi on 17/4/12.
 * JsBridge
 */
public class JsBridge {
    private String VERSION = "1.0.0";
    private String APP_NAME = "app";
    private String HOST_SUFFIX = "";
    private boolean mRefreshable = true;
    private boolean mDebugMode = BuildConfig.DEBUG;
    private int mRequestCodeSequence = 0x001;
    final private static String USER_AGENT = "android;hybrid-core:";
    private volatile static JsBridge sInstance;
    private JsHandlerFactory mJsHandlerFactory;
    private SparseArray<OnActivityResultListener> mResultListener;
    private OnWebViewInitListener mOnWebViewInitListener;
    private SparseArray<EasyPermissions.PermissionCallbacks> mPermissionsListener;
    private JsWebChromeClient.OpenFileChooserCallBack mFileChooserCallBack;
    private OnShareListener mOnShareListener;

    private JsBridge() {
        mJsHandlerFactory = new JsHandlerFactory();
        mResultListener = new SparseArray<>();
        mPermissionsListener = new SparseArray<>();
    }

    public static JsBridge getInstance() {
        if (sInstance == null) {
            synchronized (JsBridge.class) {
                if (sInstance == null) {
                    sInstance = new JsBridge();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置分享回调
     * @param listener 回调
     * @return JsBridge
     */
    public JsBridge setShareListener(OnShareListener listener) {
        sInstance.mOnShareListener = listener;
        return this;
    }


    /**
     * 设置webView 初始化监听
     * @param listener 回调
     * @return JsBridge
     */
    public JsBridge setOnWebViewInitListener(OnWebViewInitListener listener) {
        sInstance.mOnWebViewInitListener = listener;
        return this;
    }

    public static void share(final Activity activity, final HtmlConfig config) {
        if (getInstance().mOnShareListener == null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, config.toString());
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, null));
        } else {
            getInstance().mOnShareListener.share(activity, config);
        }
    }

    public String getHostSuffix() {
        return HOST_SUFFIX;
    }

    public boolean isRefreshable() {
        return mRefreshable;
    }

    public boolean isDebugMode() {
        return mDebugMode;
    }

    /**
     * @param handlerName 处理器名字
     * @param methodName 调用的方法名字
     * @return 查找处理js处理方法
     */
    public static Method findMethod(String handlerName, String methodName) {
        return getInstance().mJsHandlerFactory.findMethod(handlerName, methodName);
    }

    /**
     * 调用Native方法
     * @param webView 浏览器
     * @param message 协议串
     * @return 结果
     */
    public static boolean callNative(WebView webView, String message) {
        return JsInteract.newInstance().callNative(webView, message);
    }

    public static JsCallback getJsCallback(WebView webView, String port) {
        return JsCallback.newInstance(webView, port);
    }

    /**
     * 替换Activity中的startActivityForResult
     * @param webView 浏览器
     * @param intent 意图
     * @param listener 回调
     */
    public static void startActivityForResult(WebView webView, Intent intent, OnActivityResultListener listener) {
        int requestCode = getInstance().mRequestCodeSequence++;
        getInstance().mResultListener.put(requestCode, listener);
        if (webView != null) {
            if (webView.getContext() instanceof Activity) {
                ((Activity) webView.getContext()).startActivityForResult(intent, requestCode);
            }
        } else {
            LogUtil.d("webView is recycled.");
        }
    }

    /**
     * 需要用到startActivityForResult的时候需要调用此回调
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        OnActivityResultListener listener = getInstance().mResultListener.get(requestCode);
        if (listener != null) {
            listener.onActivityResult(resultCode, data);
            getInstance().mResultListener.remove(requestCode);
        }
    }

    /**
     * 6.0以上需要动态请求权限的时候需要调用此函数
     * @param activity 活动
     * @param rationale 申请权限的说明
     * @param listener 回调
     * @param perms 需要申请的权限
     */
    public static void requestPermissions(Activity activity, @NonNull String rationale, EasyPermissions.PermissionCallbacks listener, @NonNull String... perms) {
        int requestCode = getInstance().mPermissionsListener.size();
        getInstance().mPermissionsListener.put(requestCode, listener);
        if (activity != null) {
            EasyPermissions.requestPermissions(activity, rationale, requestCode, perms);
        } else {
            LogUtil.d("webView is recycled.");
        }
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.PermissionCallbacks listener = getInstance().mPermissionsListener.get(requestCode);
        if (listener != null) {
            // EasyPermissions handles the request result.
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
            getInstance().mPermissionsListener.remove(requestCode);
        }
    }

    /**
     * 需要在包含webView的Activity中调用
     * @param webView 浏览器
     */
    public static void onResume(WebView webView) {
        if (webView != null) {
            JsLifeCycle.onResume(webView);
        }
    }

    /**
     * 需要在包含webView的Activity中调用
     * @param webView 浏览器
     */
    public static void onPause(WebView webView) {
        if (webView != null) {
            JsLifeCycle.onPause(webView);
        }
    }

    /**
     * 需要在包含webView的Activity中调用，用来回收webView数据
     * @param webView 浏览器
     */
    public static void onDestroy(WebView webView) {
        if (webView != null) {
            JsLifeCycle.onDestroy(webView);
        }
        getInstance().mFileChooserCallBack = null;
        getInstance().mResultListener.clear();
    }

    /**
     * 初始化版本和名称，最后需要调用build方法使处理类生效
     *
     * @param version 处理类名
     * @param name    处理类类型
     * @return JsBridge
     */
    public JsBridge init(String version, String name) {
        sInstance.VERSION = version;
        sInstance.APP_NAME = name;
        return this;
    }

    /**
     * 调用Native方法
     * @param host 设置内部域名，会过滤非内部域名网页
     * @return JsBridge
     */
    public JsBridge setHost(String host) {
        sInstance.HOST_SUFFIX = host;
        return this;
    }

    /**
     * @param refreshable 是否支持刷新
     * @return JsBridge
     */
    public JsBridge refreshable(boolean refreshable) {
        sInstance.mRefreshable = refreshable;
        return this;
    }

    /**
     * @param debugMode 是否调试模式
     * @return JsBridge
     */
    public JsBridge setDebugMode(boolean debugMode) {
        sInstance.mDebugMode = debugMode;
        return this;
    }

    /**
     * 添加Js处理类，可以使用连缀的方式添加多个处理类，最后需要调用build方法使处理类生效
     *
     * @param handlerName 处理类名
     * @param clazz       处理类类型
     * @return JsBridge
     */
    public JsBridge addJsHandler(String handlerName, Class<? extends JsHandler> clazz) {
        sInstance.mJsHandlerFactory.register(handlerName, clazz);
        return this;
    }

    /**
     * 退出 和 登录 的时候调用清空cookie
     *
     * @param context 上下文
     * @return JsBridge
     */
    public JsBridge clearCookie(Context context) {
        if (context == null) {
            return this;
        }
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
        } else {
            cookieManager.removeAllCookies(null);
            CookieManager.getInstance().flush();
        }
        WebStorage.getInstance().deleteAllData(); //清空WebView的localStorage
        return this;
    }

    /**
     * 同步指定地址的cookie到webView
     *
     * @param context 上下文
     * @param url     地址
     * @param cookies cookies
     * @return JsBridge
     */
    public JsBridge syncCookie(Context context, String url, Map<String, Object> cookies) {
        if (context == null || TextUtils.isEmpty(url) || cookies == null) {
            return this;
        }

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        for (Map.Entry<String, Object> entry : cookies.entrySet()) {
            cookieManager.setCookie(url, entry.getKey() + "=" + entry.getValue());
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            cookieSyncManager.sync();
        } else {
            CookieManager.getInstance().flush();
        }
        return this;
    }

    /**
     * 设置图片选择回调到webView
     *
     * @param fileChooseCallBack 文件选择回调
     * @return JsBridge
     */
    public JsBridge setFileChooseCallBack(JsWebChromeClient.OpenFileChooserCallBack fileChooseCallBack) {
        mFileChooserCallBack = fileChooseCallBack;
        return this;
    }

    /**
     * 构建已经注册的处理类
     *
     * @param webView   浏览器
     * @param viewModel 网页处理ViewModel
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void build(WebView webView, HtmlViewModel viewModel) {
        if (webView == null) {
            throw new NullPointerException("webView is null,can't build js handler!");
        }
        getInstance().mJsHandlerFactory.build();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        String userAgentString = settings.getUserAgentString();
        String userAgentMore = ";" + APP_NAME + "-" + USER_AGENT + VERSION + ";";
        settings.setUserAgentString(userAgentString + userAgentMore);
//		settings.setPluginState(PluginState.ON_DEMAND);
        settings.setAllowFileAccess(true);
        // 设置 缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        String cacheDirPath = webView.getContext().getFilesDir().getAbsolutePath() + "/webView";
        //设置  Application Caches 缓存目录
        settings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //5.0之后h5内支持混合模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (viewModel == null) {
            viewModel = new HtmlViewModel(new HtmlViewData());
        }
        webView.setWebViewClient(new JsWebViewClient(viewModel));
        webView.setWebChromeClient(new JsWebChromeClient(webView, viewModel, mFileChooserCallBack));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(mDebugMode);
        }
        if (mOnWebViewInitListener != null) {
            mOnWebViewInitListener.onWebViewInit(webView);
        }
//        if (EasyPermissions.hasPermissions(webView.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> downloadBySystem(webView.getContext(), url, contentDisposition, mimeType));
//        } else {
        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> downloadByBrowser(url));
//        }
    }

    public interface OnActivityResultListener {
        void onActivityResult(int resultCode, Intent data);
    }

    public interface OnWebViewInitListener {
        void onWebViewInit(WebView webView);
    }

    public interface OnProgressListener {
        void onProgress(int progress);
    }

    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        ActivityUtil.navigateTo(intent);
    }

    private void downloadBySystem(Context context, String url, String contentDisposition, String mimeType) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
        request.setTitle(APP_NAME);
        // 设置通知栏的描述
        request.setDescription("下载中...");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(true);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(true);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        LogUtil.d("fileName:{}" + fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
        LogUtil.d("downloadId:{}" + downloadId);
    }
}

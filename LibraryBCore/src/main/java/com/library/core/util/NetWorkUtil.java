package com.library.core.util;

//  Created by ruibing.han on 2018/3/27.

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//网络连接状态，。。。
public class NetWorkUtil {

    private NetWorkUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取当前网络的状态
     *
     * @param context 上下文
     * @return 当前网络的状态。具体类型可参照NetworkInfo.State.CONNECTED、NetworkInfo.State.CONNECTED.DISCONNECTED等字段。当前没有网络连接时返回null
     */
    public static NetworkInfo.State getCurrentNetworkState(Context context) {
        ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (systemService != null) {
            NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.getState();
            }
        }
        return null;
    }

    /**
     * 检查WIFI是否连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null;
        }
        return false;
    }

    /**
     * 检查手机网络(4G/3G/2G)是否连接
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null;
        }
        return false;
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return false;
        }

        boolean flag = false;
        NetworkInfo[] arrayOfNetworkInfo = mConnectivityManager.getAllNetworkInfo();
        if (arrayOfNetworkInfo != null) {
            for (NetworkInfo anArrayOfNetworkInfo : arrayOfNetworkInfo) {
                if (anArrayOfNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 打开网络设置界面
     */
    public static void openNetSetting(Activity activity, int requestCode) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, requestCode);
    }
}

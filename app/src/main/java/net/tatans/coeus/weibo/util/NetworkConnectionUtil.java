package net.tatans.coeus.weibo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author 作者 ：zyk
 * @version 创建时间：2015-4-15 上午10:05:51 类说明
 */
public class NetworkConnectionUtil {
    /**
     * 创建时间：2015-4-15上午10:07:18
     * 返回值：boolean
     * 描述：网络判断
     * 作者：zhc
     */
    public static boolean hasNetworkConnection(Context context) {
        boolean hasConnectedWifi = false;
        boolean hasConnectedMobile = false;
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    hasConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    hasConnectedMobile = true;
        }
        return hasConnectedWifi || hasConnectedMobile;
    }

    /**
     * 创建时间：2015-4-15上午10:07:18
     * 返回值：boolean
     * 描述：判断wifi
     * 作者：zyk
     */
    public static boolean isWifi(Context context) {
        boolean hasConnectedWifi = false;
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    hasConnectedWifi = true;
        }
        return hasConnectedWifi;
    }

    /**
     * 创建时间：2015-4-15上午10:07:18
     * 返回值：boolean
     * 描述：判断移动数据
     * 作者：zyk
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}

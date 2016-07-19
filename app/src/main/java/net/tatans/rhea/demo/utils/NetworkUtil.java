package net.tatans.rhea.demo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.tatans.coeus.network.tools.TatansApplication;

/**
 * Created by SiLiPing on 2016/5/25.
 */
public class NetworkUtil {

	/**
	 * 判断是否有网络
	 * @return
     */
	public static boolean isNetwork() {
		ConnectivityManager connectivity = (ConnectivityManager) TatansApplication.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否使用数据流量
	 * @return
     */
	public static boolean isMobile(){
		ConnectivityManager cm = (ConnectivityManager) TatansApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo() !=null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE){
			return true;
		}
		return false;
	}

	/**
	 * 判断是否使用WiFi
	 * @return
     */
	public static boolean isWiFi(){
		ConnectivityManager cm = (ConnectivityManager) TatansApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo() !=null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI){
			return true;
		}
		return false;
	}
}

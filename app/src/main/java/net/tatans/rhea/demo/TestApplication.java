package net.tatans.rhea.demo;


import android.content.res.Configuration;

import net.tatans.coeus.network.tools.TatansApplication;
import net.tatans.coeus.network.tools.TatansLog;

/**
 * Rhea-demo [v 2.0.0]
 * classes : net.tatans.rhea.demo.TestApplication
 * yulia create at 2016/4/12 11:09
 */

public class TestApplication extends TatansApplication{
	@Override
	public void onCreate() {
		super.onCreate();
//		全局speak需要的
		setAppSpeaker();
		TatansLog.d("onCreate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		TatansLog.d("onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		TatansLog.d("onLowMemory");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		TatansLog.d("onTerminate");
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		TatansLog.d("onTrimMemory");
	}
}

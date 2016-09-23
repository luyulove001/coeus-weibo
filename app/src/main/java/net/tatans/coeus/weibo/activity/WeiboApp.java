package net.tatans.coeus.weibo.activity;

import net.tatans.coeus.network.tools.TatansApplication;

/**
 * Created by Administrator on 2016/7/21.
 */
public class WeiboApp extends TatansApplication {

    private static WeiboApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static WeiboApp getInstance() {
        return sInstance;
    }
}

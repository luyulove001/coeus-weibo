package net.tatans.coeus.tools;

import net.tatans.coeus.network.tools.TatansApplication;

/**
 * Created by Administrator on 2016/7/21.
 */
public class VoiceApp extends TatansApplication {

    private static VoiceApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
        setAppSpeaker();
    }
    public static VoiceApp getInstance() {
        return sInstance;
    }
}

package net.tatans.coeus.weibo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * Created by LCM on 2016/7/22. 10:31
 * 登录回调工具类
 */

public class  LoginUtil  {
    /** 微博授权时，启动 SSO 界面的 Activity */
    private Context mContext;
    /** 授权认证所需要的信息 */
    private AuthInfo mAuthInfo;
    /** SSO 授权认证实例 */
    private SsoHandler mSsoHandler;
    /** 微博授权认证回调 */
    private WeiboAuthListener mAuthListener;

    public LoginUtil(Context context){
        this.mContext = context;
    }

    /**
     * 设置微博授权所需信息以及回调函数。
     *
     * @param authInfo     用于保存授权认证所需要的信息
     * @param authListener 微博授权认证回调接口
     */
    public void setWeiboAuthInfo(AuthInfo authInfo, WeiboAuthListener authListener) {
        mAuthInfo = authInfo;
        mAuthListener = authListener;
        if (null == mSsoHandler && mAuthInfo != null) {
            mSsoHandler = new SsoHandler((Activity)mContext, mAuthInfo);
        }

        if (mSsoHandler != null) {
            mSsoHandler.authorize(mAuthListener);
        } else {
            Log.e("TAG", "Please setWeiboAuthInfo(...) for first");
        }
    }
}

package net.tatans.coeus.weibo.base;


import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import net.tatans.coeus.weibo.activity.WeiboApp;
import net.tatans.coeus.weibo.bean.AccountBean;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;

/**
 * Created by wangdan on 15/12/13.
 */
public class AppContext {

    private static AccountBean mAccount;

    public static boolean isLoggedIn() {
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(WeiboApp.getContext());
        return mAccount != null && accessToken != null;
    }

    public static void logout() {
        mAccount = null;
    }

    public static void login(AccountBean accountBean) {
        AppContext.setAccount(accountBean);

        // 未读消息重置
//        if (AppContext.getAccount().getUnreadCount() == null) {
//            AppContext.getAccount().setUnreadCount(UnreadService.getUnreadCount());
//        }
//        if (AppContext.getAccount().getUnreadCount() == null) {
//            AppContext.getAccount().setUnreadCount(new UnreadCount());
//        }
//        // 开启未读服务
//        UnreadService.startService();

//        // 刷新定时任务
//        MyApplication.refreshPublishAlarm();

//        // 处理点赞数据
//        DoLikeAction.refreshLikeCache();

    }

    public static void setAccount(AccountBean account) {
        mAccount = account;
    }

    public static AccountBean getAccount() {
        return mAccount;
    }

    public static void clearCookie() {
        mAccount.setCookie(null);

//        AccountUtils.updateAccount(mAccount);
//        AccountUtils.setLogedinAccount(mAccount);
    }

}

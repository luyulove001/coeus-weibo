package net.tatans.coeus.weibo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.coeus.weibo.util.LoginUtil;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

import java.text.SimpleDateFormat;

/**
 * Created by LCM on 2016/7/22. 13:31
 * 登录activity
 */
@ContentView(R.layout.login)
public class LoginActivity extends BaseActivity {
    @ViewIoc(R.id.login)
    private TextView mLogin;//登录
    @ViewIoc(R.id.register)
    private TextView mRegister;//注册

    private AuthInfo mAuthInfo;

    // 登陆认证对应的listener
    private AuthListener mLoginListener = new AuthListener();
    private LoginUtil mLoginUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AccessTokenKeeper.readAccessToken(this).getToken().equals("") && AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();//登录成功后finish掉登录页面
        }
        //创建授权认证信息
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mLoginUtil = new LoginUtil(this);
    }

    /**
     * 登录
     */
    @OnClick(R.id.login)
    public void Login() {
        mLoginUtil.setWeiboAuthInfo(mAuthInfo, mLoginListener);
    }

    /**
     * 注册
     */
    @OnClick(R.id.register)
    public void Register() {
        if (isPkgInstalled("com.sina.weibo")) {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName("com.sina.weibo", "com.sina.weibo.SplashActivity");
            intent.setComponent(componentName);
            startActivity(intent);
        } else {
            TatansToast.showAndCancel("请下载新浪微博客户端或新浪微博官网进行注册");
        }
    }

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();//登录成功后finish掉登录页面
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            TatansToast.showAndCancel(e.getMessage());
        }

        @Override
        public void onCancel() {
            TatansToast.showAndCancel("取消授权");
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginUtil.authorizeCallBack(requestCode, resultCode, data);
    }


    /**
     * 判断某应用是否存在
     *
     * @param pkgName 应用的包名
     * @return
     */
    private boolean isPkgInstalled(String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
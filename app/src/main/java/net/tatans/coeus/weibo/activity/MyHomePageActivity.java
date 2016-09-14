package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.coeus.weibo.util.RequestWeiboData;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

/**
 * Created by LCM on 2016/8/4. 13:36
 * 我的主页
 */
@ContentView(R.layout.personal_info)
public class MyHomePageActivity extends BaseActivity {
    //获取视图
    @ViewIoc(R.id.name)
    private TextView name;
    @ViewIoc(R.id.gender)
    private TextView gender;
    @ViewIoc(R.id.friends)
    private TextView friends;
    @ViewIoc(R.id.followers)
    private TextView followers;
    @ViewIoc(R.id.verified)
    private TextView verified;
    @ViewIoc(R.id.address)
    private TextView address;
    @ViewIoc(R.id.company)
    private TextView company;
    @ViewIoc(R.id.home_page_listview)
    private PullToRefreshListView pullToRefreshListView;
    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 获取用户的个人信息
     */
    private UsersAPI mUserApi;
    private RequestWeiboData weiboData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    /**
     * 初始化
     */
    private void initData() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mUserApi = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        long uid = getIntent().getLongExtra(Const.UID, Long.parseLong(mAccessToken.getUid()));
        weiboData = new RequestWeiboData(this, pullToRefreshListView, uid);
        mUserApi.show(uid, mListener);
        weiboData.RequestData();
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            User user = User.parse(response);
            if (user != null) {
                Message message = new Message();
                message.obj = user;
                message.what = 1;
                mHandler.handleMessage(message);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };

    /**
     * 更新界面ui
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                User user = (User) msg.obj;
                name.setText(user.screen_name);
                if (user.gender.equals("m")) {
                    gender.setText("男");
                } else if (user.gender.equals("f")) {
                    gender.setText("女");
                } else {
                    gender.setText("未知");
                }
                friends.setText("关注:" + user.friends_count);
                if (getIntent().getStringExtra("friends_count") != null) {
                    friends.setText("关注:" + getIntent().getStringExtra("friends_count"));
                }
                followers.setText("粉丝:" + user.followers_count);
                if (getIntent().getStringExtra("followers_count") != null)
                    followers.setText("粉丝:" + getIntent().getStringExtra("followers_count"));//上级页面传下来，现在是搜索结果页面
                if (user.verified) {
                    verified.setText("微博认证:" + user.verified_reason);
                    if (getIntent().getStringExtra("verified_reason") != null)
                        verified.setText("微博认证:" + getIntent().getStringExtra("verified_reason").trim());//上级页面传下来，现在是搜索结果页面
                } else {
                    verified.setText("微博认证:未认证");
                }
                address.setText("所在地:" + user.location);
                if (user.description != null && !user.description.equals("")) {
                    company.setText("简介:" + user.description);
                } else {
                    company.setText("简介:无");
                }
                if (getIntent().getStringExtra("description") != null && !getIntent().getStringExtra("description").equals(""))
                    company.setText("简介:" + getIntent().getStringExtra("description"));//上级页面传下来，现在是搜索结果页面
            }
        }
    };

}

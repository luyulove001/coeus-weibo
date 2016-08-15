package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.StatusAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LCM on 2016/7/28. 14:43
 *
 * @我的
 */
@ContentView(R.layout.remind_me)
public class RemindMeActivity extends BaseActivity {

    private Oauth2AccessToken accessToken;

    private StatusesAPI mStatusesApi;

    private StatusAdapter adapter;
    @ViewIoc(R.id.home_page_listview)
    private PullToRefreshListView pullToRefresh;

    private  List<StatusList> statuslists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    /**
     * 初始化
     */
    private void initData() {
        statuslists = new ArrayList<StatusList>();
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mStatusesApi = new StatusesAPI(this, Constants.APP_KEY, accessToken);
        Long uid = Long.parseLong(accessToken.getUid());
        mStatusesApi.mentions(0L, 0L, 50, 1,0, 0, 0, false, mListener);
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.e("status", response);
            StatusList status = StatusList.parse(response);
            adapter = new StatusAdapter(RemindMeActivity.this, status, Const.REMIND);
            pullToRefresh.setAdapter(adapter);
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    };


}

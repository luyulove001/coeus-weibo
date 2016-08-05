package net.tatans.coeus.weibo.util;

import android.content.Context;
import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.weibo.adapter.HomeFragmentAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LCM on 2016/8/5. 8:40
 * 获取用户所发微博的数据
 */

public class RequestWeiboData {

    public Context mContext;

    private PullToRefreshListView pullToRefresh;
    /**
     * 获取当前的Token
     */
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatuses;

    private HomeFragmentAdapter adapter;

    public RequestWeiboData(Context context, PullToRefreshListView pullToRefreshListView) {
        this.mContext = context;
        this.pullToRefresh = pullToRefreshListView;
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mStatuses = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
    }

    /**
     * 请求数据
     */
    public void RequestData() {
        Long uid = Long.parseLong(mAccessToken.getUid());
        mStatuses.userTimeline(uid,0, 0, 100, 1, false, 0, false, mListener);
    }

    /*
     *微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.e("onComplete", response);
            StatusList status = StatusList.parse(response);
            List<StatusList> statuslists = new ArrayList<StatusList>();
            statuslists.add(status);
            adapter = new HomeFragmentAdapter(mContext, statuslists, mAccessToken);
            pullToRefresh.setAdapter(adapter);
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };
}

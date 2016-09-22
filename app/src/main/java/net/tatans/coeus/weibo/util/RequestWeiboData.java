package net.tatans.coeus.weibo.util;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.adapter.StatusAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;

/**
 * Created by LCM on 2016/8/5. 8:40
 * 获取用户所发微博的数据
 */

public class RequestWeiboData {

    public Context mContext;
    private boolean isRefresh = false;
    private boolean isEnd = false;
    private StatusList statuses = new StatusList();
    /**
     * 加载页数控制
     */
    private int index = 1;

    private PullToRefreshListView pullToRefresh;
    /**
     * 获取当前的Token
     */
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatuses;

    private StatusAdapter adapter;
    private long mUid;

    public RequestWeiboData(Context context, PullToRefreshListView pullToRefreshListView, long uid) {
        this.mContext = context;
        this.pullToRefresh = pullToRefreshListView;
        this.mUid = uid;
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        mStatuses = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
        //下拉刷新接口
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mStatuses.userTimeline(mUid, 0L, 0L, 20, 1, false, 0, false, mListener);
                pullToRefresh.setRefreshing();
                isRefresh = true;
                isEnd = false;
                index = 1;
            }
        });
        //到底部加载更多接口
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (isEnd) {
                    TatansToast.showAndCancel("没有更多内容了");
                } else {
                    index += 1;
                    mStatuses.userTimeline(mUid, 0L, 0L, 20, index, false, 0, false, mListener);
                    TatansToast.showAndCancel("加载第" + index + "页");
                    isRefresh = false;
                }
            }
        });
    }

    /**
     * 请求数据
     */
    public void RequestData() {
        mStatuses.userTimeline(mUid, 0, 0, 20, index, false, 0, false, mListener);
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.e("onComplete", response);
            StatusList status1 = StatusList.parse(response);
            if (status1.statusList == null || status1.statusList.isEmpty()) {
                isEnd = true;
                return;
            }
            if (statuses.statusList == null || statuses.statusList.isEmpty() || isRefresh) {
                statuses = status1;
                adapter = new StatusAdapter(mContext, statuses,Const.MY_HOME_PAGE);
                pullToRefresh.setAdapter(adapter);
                pullToRefresh.onRefreshComplete();
            } else {
                statuses.statusList.addAll(status1.statusList);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            TatansToast.showAndCancel(info.toString());
        }
    };
}

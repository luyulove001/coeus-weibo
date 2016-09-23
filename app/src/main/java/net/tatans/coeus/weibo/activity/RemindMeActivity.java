package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.tools.TatansActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.view.ViewInject;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.StatusAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;

/**
 * Created by LCM on 2016/7/28. 14:43
 *
 * @我的
 */
public class RemindMeActivity extends TatansActivity {

    private Oauth2AccessToken accessToken;

    private StatusesAPI mStatusesApi;

    private StatusAdapter adapter;
    @ViewInject(id = R.id.home_page_listview)
    private PullToRefreshListView pullToRefresh;
    private StatusList status = new StatusList();
    private boolean isRefresh = false, isEnd = false;
    private int index = 1;//当前页数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_me);
        initData();
    }

    /**
     * 初始化
     */
    private void initData() {
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mStatusesApi = new StatusesAPI(this, Constants.APP_KEY, accessToken);
        mStatusesApi.mentions(0L, 0L, 50, index, 0, 0, 0, false, mListener);
        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(RemindMeActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                index = 1;
                isRefresh = true;
                isEnd = false;
                if (status.statusList != null)
                    status.statusList.clear();
                pullToRefresh.setRefreshing();
                mStatusesApi.mentions(0L, 0L, 50, index, 0, 0, 0, false, mListener);
            }
        });
        pullToRefresh.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (isEnd) {
                    TatansToast.showAndCancel("没有更多内容了");
                    return;
                }
                isRefresh = false;
                index += 1;
                mStatusesApi.mentions(0L, 0L, 50, index, 0, 0, 0, false, mListener);
            }
        });
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.e("status", response);
            pullToRefresh.onRefreshComplete();
            StatusList status1 = StatusList.parse(response);
            if (status1.statusList == null || status1.statusList.isEmpty()) {
                isEnd = true;
                TatansToast.showAndCancel("未请求到数据");
                return;
            }
            if (status1.statusList.size() < 50)
                isEnd = true;
            if (status.statusList == null || status.statusList.isEmpty() || isRefresh) {
                status = status1;
                adapter = new StatusAdapter(RemindMeActivity.this, status, Const.REMIND);
                pullToRefresh.setAdapter(adapter);
            } else {
                status.statusList.addAll(status1.statusList);
                adapter.notifyDataSetChanged();
                TatansToast.showAndCancel("加载第" + index + "页");
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    };


}

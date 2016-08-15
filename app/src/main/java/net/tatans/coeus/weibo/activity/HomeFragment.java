package net.tatans.coeus.weibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.StatusAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;


public class HomeFragment extends Fragment {
    private PullToRefreshListView pullToRefreshListView;
    /**
     * 微博列表适配器
     */
    private StatusAdapter adapter;
    /**
     * 微博列表数据
     */
    private StatusList statuses;
    /**
     * 是否刷新
     */
    private boolean isRefresh = false;
    /**
     * 判断是否加载到最后了
     */
    private boolean isEnd = false;
    private MainActivity mActivity;
    private View view;
    /**
     * 加载的页数
     */
    private int index = 1;
    private String regex = "http://t.cn/[a-zA-Z0-9]+";
    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 用于获取微博信息流等操作的API
     */
    private StatusesAPI mStatusesAPI;

    /**
     * 刷新列表数据
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    pullToRefreshListView.onRefreshComplete();
                    System.out.println(statuses.statusList.size());
                    break;
                case 2:
                    pullToRefreshListView.setRefreshing();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
        mActivity.setHandler(mHandler);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_page, container, false);
        initView();
        initData();
        return view;
    }

    private void initData() {
        statuses = new StatusList();
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, mAccessToken);
        mStatusesAPI.friendsTimeline(0L, 0L, 20, 1, false, 0, false, mListener);
        //下拉刷新接口
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                TatansToast.showAndCancel("刷新" + label);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                mHandler.sendEmptyMessage(2);
                mStatusesAPI.friendsTimeline(0L, 0L, 20, 1, false, 0, false, mListener);
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
                    mStatusesAPI.friendsTimeline(0L, 0L, 20, index, false, 0, false, mListener);
                    TatansToast.showAndCancel("加载第" + index + "页");
                    isRefresh = false;
                }
            }
        });
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

    }

    /**
     * 初始化
     */
    private void initView() {
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.home_page_listview);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 可见时执行的操作

        } else {
            // 不可见时执行的操作
        }
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses1 = StatusList.parse(response);
                    //当返回数据为空时代表没有更多了
                    if (statuses1.statusList == null || statuses1.statusList.isEmpty()) {
                        isEnd = true;
                        return;
                    }
                    if (statuses.statusList == null || statuses.statusList.isEmpty() || isRefresh) {
                        statuses = statuses1;
                        adapter = new StatusAdapter(getActivity(), statuses, Const.HOME);
                        pullToRefreshListView.setAdapter(adapter);
                    } else {
                        statuses.statusList.addAll(statuses1.statusList);
                    }
                    mHandler.sendEmptyMessage(1);
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(getActivity(), info.toString(), Toast.LENGTH_LONG).show();
        }
    };


}

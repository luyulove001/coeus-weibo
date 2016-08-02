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
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.HomeFragmentAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HomeFragment extends Fragment {
    private PullToRefreshListView pullToRefreshListView;

    private HomeFragmentAdapter adapter;
    private List<StatusList> list;
    private MainActivity mActivity;
    private View view;
    private int moreLoad=0;
    private String regex ="http://t.cn/[a-zA-Z0-9]+";
    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 用于获取微博信息流等操作的API
     */
    private StatusesAPI mStatusesAPI;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new HomeFragmentAdapter(getActivity(), list,mAccessToken);
                    pullToRefreshListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pullToRefreshListView.onRefreshComplete();
                    break;
                case 2:
                    pullToRefreshListView.setRefreshing();
                break;
                case 3:
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
        //将用户信息放到集合中
        list = new ArrayList<StatusList>();
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, mAccessToken);
        mStatusesAPI.friendsTimeline(0L, 0L,200, 1, false,0, false, mListener);

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                list = new ArrayList<StatusList>();
                mStatusesAPI.friendsTimeline(0L, 0L, 200, 1, false,0, false, mListener);
            }
        });
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                moreLoad+=1;
                mHandler.sendEmptyMessage(3);
            }
        });

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
                    StatusList statuses = StatusList.parse(response);
                    list.add(statuses);
                    mHandler.sendEmptyMessage(1);
//                    for (int i=0;i<statuses.statusList.size();i++){
//
//
//                        if (statuses.statusList.get(i).retweeted_status==null){
//                        }
//                        else{
//                            if (statuses.statusList.get(i).retweeted_status.user == null){
//
//                            }
//                            else{
//                               String vv= statuses.statusList.get(i).retweeted_status.text;
//                                System.out.println(vv);
//                                if (statuses.statusList.get(i).retweeted_status.text.equals("")){
//                                }
//                                else{
//                                    Pattern pt_me = Pattern.compile(regex);
//                                    Matcher mt_me = pt_me.matcher(statuses.statusList.get(i).retweeted_status.text);
//
//                                    while (mt_me.find()) {
//                                        String vvv = mt_me.group(0);
//                                        System.out.println(vvv);
//                                    }
//                                }
//                            }
//                        }
//
//                    }
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

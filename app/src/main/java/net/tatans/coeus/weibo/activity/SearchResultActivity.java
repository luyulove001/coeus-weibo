package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansLog;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.StatusAdapter;
import net.tatans.coeus.weibo.adapter.UserSearchAdapter;
import net.tatans.coeus.weibo.bean.SearchResultUser;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.rhea.network.view.ContentView;

import java.util.ArrayList;

/**
 * 用于显示查询出来的结果
 * Created by cly on 2016/7/27.
 */
@ContentView(R.layout.home_page)
public class SearchResultActivity extends BaseActivity {
    private static final String REQUEST_USER = "http://m.weibo.cn/page/pageJson";
    private PullToRefreshListView listView;
    /**
     * 判断是否是搜索微博
     */
    private boolean isStatues;
    /**
     * 网络请求
     */
    private TatansHttp http = new TatansHttp();
    /**
     * 请求参数
     */
    private HttpRequestParams params;
    /**
     * 请求返回的user数据
     */
    private ArrayList<SearchResultUser> resultUsers = new ArrayList<SearchResultUser>();
    /**
     * 请求返回的status数据
     */
    private ArrayList<Status> resultStatus;
    private StatusList statusList;
    /**
     * 请求user回调,请求status回调
     */
    private HttpRequestCallBack searchUserCallBack, searchStatusCallBack;
    /**
     * 显示搜索到的user的适配器
     */
    private UserSearchAdapter userAdapter;
    /**
     * 显示搜索到的微博的适配器
     */
    private StatusAdapter statusAdapter;
    /**
     * 请求到的最大页数
     */
    private int maxPage;
    /**
     * 加载页数控制
     */
    private int index = 1;
    private String content;
    private boolean isRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (PullToRefreshListView) findViewById(R.id.home_page_listview);
        isStatues = getIntent().getBooleanExtra("isStatues", true);//搜索类型 true 微博; false 用户
        content = getIntent().getStringExtra("content");//搜索内容
        statusList = new StatusList();
        initPull2RefreshListener();
        initCallBack();
        requestData();
    }

    private void initPull2RefreshListener() {
        //下拉刷新接口
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                isRefresh = true;
                resultUsers.clear();
                listView.setRefreshing();
                requestData();
            }
        });
        //到底部加载更多接口
        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (index >= maxPage) {
                    TatansToast.showAndCancel("没有更多内容了");
                } else {
                    index += 1;
                    requestData();
                    TatansToast.showAndCancel("加载第" + index + "页");
                    isRefresh = false;
                }
            }
        });
    }

    /**
     * 请求数据
     */
    private void requestData() {
        if (isStatues) {
            params = new HttpRequestParams();
            params.put("containerid", "100103type%3D2%26q%3D" + content);
            params.put("page", String.valueOf(index));
            http.get(REQUEST_USER, params, searchStatusCallBack);
        } else {
            params = new HttpRequestParams();
            params.put("containerid", "100103type%3D3%26q%3D" + content);
            params.put("page", String.valueOf(index));
            http.get(REQUEST_USER, params, searchUserCallBack);
        }
    }

    /**
     * 初始化http请求的回调
     */
    private void initCallBack() {
        searchUserCallBack = new HttpRequestCallBack<String>() {
            @Override
            public void onSuccess(String users) {
                super.onSuccess(users);
                JSONObject responseJSON = JSONObject.parseObject(users);
                int ok = responseJSON.getInteger("ok");
                maxPage = responseJSON.getInteger("maxPage");
                if (ok == 1) {
                    JSONArray cardsArray = responseJSON.getJSONArray("cards");
                    if (cardsArray.size() > 0) {
                        JSONObject cardGroupsObject;
                        if (index > 1)
                            cardGroupsObject = cardsArray.getJSONObject(0);
                        else
                            cardGroupsObject = cardsArray.getJSONObject(1);
                        JSONArray cardGroupArray = cardGroupsObject.getJSONArray("card_group");
                        for (int i = 0; i < cardGroupArray.size(); i++) {
                            JSONObject cardGroup = cardGroupArray.getJSONObject(i);
                            SearchResultUser user = new SearchResultUser();
                            user.setDesc1(cardGroup.getString("desc1"));
                            user.setDesc2(cardGroup.getString("desc2"));
                            JSONObject userJSON = cardGroup.getJSONObject("user");
                            user.setId(userJSON.getString("id"));
                            user.setFollowing(userJSON.getBoolean("following"));
                            user.setFollow_me(userJSON.getBoolean("follow_me"));
                            user.setFansNum(userJSON.getString("fansNum"));
                            user.setScreen_name(userJSON.getString("screen_name"));
                            user.setDescription(userJSON.getString("description"));
                            user.setProfile_image_url(userJSON.getString("profile_image_url"));
                            user.setStatuses_count(userJSON.getInteger("statuses_count"));
                            user.setGender(userJSON.getString("gender"));
                            user.setRemark(userJSON.getString("remark"));
                            user.setVerified(userJSON.getBoolean("verified"));
                            user.setVerified_reason(userJSON.getString("verified_reason").trim());
                            TatansLog.d(user.toString());
                            resultUsers.add(user);
                        }
                    }
                    if (resultUsers == null || resultUsers.isEmpty() || isRefresh) {
                        userAdapter = new UserSearchAdapter(SearchResultActivity.this, resultUsers);
                        listView.setAdapter(userAdapter);
                        listView.onRefreshComplete();
                    } else {
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                TatansLog.d(strMsg);
            }
        };
        searchStatusCallBack = new HttpRequestCallBack<String>() {
            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }

            @Override
            public void onSuccess(String status) {
                super.onSuccess(status);
                try {
                    JSONObject responseJSON = JSONObject.parseObject(status);
                    int ok = responseJSON.getInteger("ok");
                    maxPage = responseJSON.getInteger("maxPage");
                    resultStatus = new ArrayList<Status>();
                    if (ok == 1) {
                        JSONArray cardsArray = responseJSON.getJSONArray("cards");
                        ArrayList<String> picUrls;
                        for (int i = 0; i < cardsArray.size(); i++) {
                            JSONObject cardGroupsObject = cardsArray.getJSONObject(i);
                            JSONArray cardGroupArray = cardGroupsObject.getJSONArray("card_group");
                            for (int j = 0; j < cardGroupArray.size(); j++) {
                                JSONObject cardGroup = cardGroupArray.getJSONObject(j);

                                JSONObject mblogObject = cardGroup.getJSONObject("mblog");

                                Status content = JSON.parseObject(mblogObject.toJSONString(), Status.class);
                                // 图片
                                if (mblogObject.containsKey("pics")) {
                                    JSONArray picsArray = mblogObject.getJSONArray("pics");
                                    if (picsArray != null && !picsArray.isEmpty()) {
                                        picUrls = new ArrayList<String>();
                                        String url;
                                        for (int k = 0; k < picsArray.size(); k++) {
                                            url = picsArray.getJSONObject(k).getString("url");
                                            picUrls.add(url);
                                        }
                                        content.setPic_urls(picUrls);
                                    }
                                }
                                if (mblogObject.containsKey("retweeted_status")) {
                                    JSONObject retweeted_status = mblogObject.getJSONObject("retweeted_status");
                                    if (retweeted_status.containsKey("pics")) {
                                        JSONArray picsArray = retweeted_status.getJSONArray("pics");
                                        if (picsArray != null && !picsArray.isEmpty()) {
                                            picUrls = new ArrayList<String>();
                                            String url;
                                            for (int k = 0; k < picsArray.size(); k++) {
                                                url = picsArray.getJSONObject(k).getString("url");
                                                picUrls.add(url);
                                            }
                                            content.retweeted_status.setPic_urls(picUrls);
                                        }
                                    }
                                }
                                // 把Html5文本转换一下
                                content.setText(Html.fromHtml(content.getText()).toString());
                                if (content.getRetweeted_status() != null) {
                                    content.getRetweeted_status().setText(Html.fromHtml(content.getRetweeted_status().getText()).toString());
                                }
                                TatansLog.d("antony", content.user.toString() + "---");
                                resultStatus.add(content);
                            }
                        }
                        if (statusList.statusList == null || statusList.statusList.isEmpty() || isRefresh) {
                            statusList.statusList = resultStatus;
                            statusAdapter = new StatusAdapter(SearchResultActivity.this, statusList, Const.SEARCH);
                            listView.setAdapter(statusAdapter);
                        } else {
                            statusList.statusList.addAll(resultStatus);
                            statusAdapter.notifyDataSetChanged();
                        }
                        listView.onRefreshComplete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}

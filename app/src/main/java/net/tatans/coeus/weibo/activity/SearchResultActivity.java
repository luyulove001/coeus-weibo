package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.text.Html;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import net.tatans.coeus.network.callback.HttpRequestCallBack;
import net.tatans.coeus.network.callback.HttpRequestParams;
import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansHttp;
import net.tatans.coeus.network.tools.TatansLog;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.bean.PicUrls;
import net.tatans.coeus.weibo.bean.SearchResultUser;
import net.tatans.coeus.weibo.bean.StatusContent;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.view.ContentView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 用于显示查询出来的结果
 * Created by cly on 2016/7/27.
 */
@ContentView(R.layout.home_page)
public class SearchResultActivity extends BaseActivity {
    private static final String REQUEST_USER = "http://m.weibo.cn/page/pageJson";
    private PullToRefreshListView listView;
    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 用于获取微博信息流等操作的API
     */
    private StatusesAPI mStatusesAPI;
    /**
     * 用于获取查找用户等操作的API
     */
    private UsersAPI mUsersAPI;
    /**
     * 搜索用户回调，搜索微博回调
     */
    private RequestListener userRequest, statuseRequest;
    /**
     * 判断是否是搜索微博
     */
    private boolean isStatues;
    private TatansHttp http = new TatansHttp();
    private HttpRequestParams params = new HttpRequestParams();
    private ArrayList<SearchResultUser> resultUsers = new ArrayList<SearchResultUser>();
    private ArrayList<StatusContent> resultStatus = new ArrayList<StatusContent>();
    private HttpRequestCallBack searchUserCallBack;
    private HttpRequestCallBack searchStatusCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (PullToRefreshListView) findViewById(R.id.home_page_listview);
        initCallBack();
        initData();
    }

    private void initData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        isStatues = getIntent().getBooleanExtra("isStatues", true);
        userRequest = new RequestListener() {
            @Override
            public void onComplete(String s) {
                User user = User.parse(s);
                TatansToast.showAndCancel(s);
                TatansLog.d(s);
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        };
        if (isStatues) {
            // TODO: 2016/7/28 搜索微博
            String statusContent = getIntent().getStringExtra("content");
            params.put("containerid", "100103type%3D2%26q%3D" + statusContent + "&page=1");
            http.get(REQUEST_USER, params, searchStatusCallBack);
        } else {
            String screenName = getIntent().getStringExtra("content");
            params.put("containerid", "100103type%3D3%26q%3D" + screenName + "&page=1");
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
                if (ok == 1) {
                    JSONArray cardsArray = responseJSON.getJSONArray("cards");
                    if (cardsArray.size() > 0) {
                        JSONObject cardGroupsObject = cardsArray.getJSONObject(1);
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
                            TatansLog.d(user.toString());
                            resultUsers.add(user);
                        }
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
                    if (ok == 1) {
                        JSONArray cardsArray = responseJSON.getJSONArray("cards");
                        for (int i = 0; i < cardsArray.size(); i++) {
                            JSONObject cardGroupsObject = cardsArray.getJSONObject(i);
                            JSONArray cardGroupArray = cardGroupsObject.getJSONArray("card_group");
                            for (int j = 0; j < cardGroupArray.size(); j++) {
                                JSONObject cardGroup = cardGroupArray.getJSONObject(j);

                                JSONObject mblogObject = cardGroup.getJSONObject("mblog");

                                StatusContent content = JSON.parseObject(mblogObject.toJSONString(), StatusContent.class);
                                // 图片
                                if (mblogObject.containsKey("pics")) {
                                    JSONArray picsArray = mblogObject.getJSONArray("pics");
                                    if (picsArray != null && picsArray.size() > 0) {
                                        PicUrls picUrls = new PicUrls();
                                        picUrls.setThumbnail_pic(picsArray.getJSONObject(0).getString("url"));
                                        content.setPic_urls(new PicUrls[]{picUrls});
                                    }
                                }
                                // 把Html5文本转换一下
                                content.setText(Html.fromHtml(content.getText()).toString());
                                if (content.getRetweeted_status() != null) {
                                    content.getRetweeted_status().setText(Html.fromHtml(content.getRetweeted_status().getText()).toString());
                                }
                                // 把时间转换一下
                                try {
                                    Calendar calendar = Calendar.getInstance();
                                    int year = calendar.get(Calendar.YEAR);
                                    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                                    calendar.setTimeInMillis(format.parse(content.getCreated_at()).getTime());
                                    calendar.set(Calendar.YEAR, year);
                                    content.setCreated_at(calendar.getTimeInMillis() + "");
                                } catch (ParseException e) {
                                    try {
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        content.setCreated_at(format.parse(content.getCreated_at()).getTime() + "");
                                    } catch (ParseException ewe) {
                                    }
                                }
                                TatansLog.d(content.toString());
                                resultStatus.add(content);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }
}

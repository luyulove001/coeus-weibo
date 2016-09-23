package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.User;

import net.tatans.coeus.network.tools.TatansActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.view.ViewInject;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.ContactListAdapter;
import net.tatans.coeus.weibo.bean.ContactList;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LCM on 2016/7/25. 13:17
 * 联系人,粉丝列表
 */
public class ContactListActivity extends TatansActivity {
    //获取视图
    @ViewInject(id = R.id.edt_search)
    private EditText mEdtSearch;
    @ViewInject(id = R.id.refresh_listview)
    private PullToRefreshListView refresh_listview;
    @ViewInject(id = R.id.con_or_follow)
    private TextView con_or_follow;
    /**
     * 字体高度
     */
    List<User> list = new ArrayList<User>();
    private ContactListAdapter adapter;
    //获取用户
    private FriendshipsAPI mFriendshipsAPI;
    private Oauth2AccessToken accessToken;
    //搜索框，搜索时udpate的list
    private List<User> listString = new ArrayList<User>();
    //判断是从关注进入联系人还是从@进入
    private int mConOrFollow;
    private boolean isEnd = false;
    private long uid;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_activity);
        initData();
        initViewEvent();
        RequestData();
        Refresh();

    }

    /**
     * 上拉加载，下拉刷新
     */
    private void Refresh() {
        refresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                if (mConOrFollow == 1 || mConOrFollow == 0) {
                    mFriendshipsAPI.friends(uid, 200, 0, true, mListener);
                } else {
                    mFriendshipsAPI.followers(uid, 200, 0, true, mListener);
                }
                refresh_listview.setRefreshing();
                isEnd = false;
                index = 1;
            }
        });
        refresh_listview.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (mConOrFollow == 1 || mConOrFollow == 0) {
                    if (!isEnd) {
                        mFriendshipsAPI.friends(uid, 200, index, true, mListener);
                        index += 1;
                    } else {
                        TatansToast.showAndCancel("没有更多内容了");
                    }
                } else {
                    if (!isEnd) {
                        mFriendshipsAPI.followers(uid, 200, index, true, mListener);
                        index += 1;
                    } else {
                        TatansToast.showAndCancel("没有更多内容了");
                    }

                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mConOrFollow = getIntent().getExtras().getInt(Const.CONTACT_OR_FOllOW);
        // 获取当前已保存过的 Token
        accessToken = AccessTokenKeeper.readAccessToken(this);
        uid = Long.parseLong(accessToken.getUid());
        //实例化关系类
        mFriendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY, accessToken);

        if (mConOrFollow == 0) {
            con_or_follow.setText("关注");
        }

    }

    /**
     * 初始化布局数据
     */
    private void initViewEvent() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listString.clear();
                if (mEdtSearch.getText().toString() != null) {
                    String input_info = mEdtSearch.getText().toString();
                    adapter = new ContactListAdapter(ContactListActivity.this, getNewData(input_info));
                    adapter.setmType(mConOrFollow);
                    refresh_listview.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 请求关注人数据
     */
    private void RequestData() {
        switch (mConOrFollow) {
            case 0:
                mFriendshipsAPI.friends(uid, 200, 0, true, mListener);
                break;
            case 1:
                mFriendshipsAPI.friends(uid, 200, 0, true, mListener);
                break;
            case 2:
                mFriendshipsAPI.followers(uid, 200, 0, true, mListener);
                break;
            default:
                break;
        }
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ContactList contactList = ContactList.parse(response);
            if (contactList.contactList == null || contactList.contactList.isEmpty()) {
                isEnd = true;
                return;
            }
            if (list.size() == 0 || list.isEmpty()) {
                list = contactList.contactList;
                adapter = new ContactListAdapter(ContactListActivity.this, list);
                adapter.setmType(mConOrFollow);
                refresh_listview.setAdapter(adapter);
            } else {
                User userEnd = contactList.contactList.get(contactList.contactList.size() - 1);
                User user = list.get(list.size() - 1);
                if (!user.screen_name.equals(userEnd.screen_name)) {
                    list.add(userEnd);
                }
                adapter.notifyDataSetChanged();
            }
            refresh_listview.onRefreshComplete();

        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    };

    /**
     * 根据文本框中关键词 更新adapter
     *
     * @param input_info
     * @return
     */
    private List<User> getNewData(String input_info) {
        for (int i = 0; i < list.size(); i++) {
            String screen_name = list.get(i).screen_name;
            if (screen_name.contains(input_info)) {
                listString.add(list.get(i));
            }
        }
        return listString;
    }
}

package net.tatans.coeus.weibo.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.ContactListAdapter;
import net.tatans.coeus.weibo.adapter.FollowersAdapter;
import net.tatans.coeus.weibo.bean.ContactList;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

import java.util.ArrayList;
import java.util.List;

import static net.tatans.coeus.weibo.R.id.listView;

/**
 * Created by LCM on 2016/7/27. 9:55
 */
@ContentView(R.layout.sort_activity)
public class FollowersActivity extends BaseActivity {
    //获取视图
    @ViewIoc(R.id.con_or_follow)
    private TextView con_or_follow;
    @ViewIoc(listView)
    private ListView mListView;
    @ViewIoc(R.id.edt_search)
    private EditText mEdtSearch;

    private List<String>  mFollowList;

    //获取用户
    private FriendshipsAPI mFriendships;
    private Oauth2AccessToken  mAccessToken;

    private FollowersAdapter adapter;

    private List<String>  screenList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        RequestData();
    }

    /**
     * 请求粉丝数据
     */
    private void RequestData() {
        long uid = Long.parseLong(mAccessToken.getUid());
        mFriendships.followers(uid, 200, 0, true, mListener);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mFollowList = new ArrayList<String>();
        //获取用户保存的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        //实例化关系类
        mFriendships = new FriendshipsAPI(this,Constants.APP_KEY,mAccessToken);

        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                screenList.clear();
                if(mEdtSearch.getText().toString() != null){
                    String input_info = mEdtSearch.getText().toString();
                    screenList = getNewData(input_info);
                    adapter = new FollowersAdapter(FollowersActivity.this,screenList);
                    mListView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ContactList contactList = ContactList.parse(response);
            for (int i = 0; i < contactList.contactList.size(); i++) {
                mFollowList.add(contactList.contactList.get(i).screen_name);
            }
            setListData(mFollowList);
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    };

    /**
     * 填充数据并加载adapter
     *
     * @param listData
     */
    public void setListData(List<String> listData) {
        adapter = new FollowersAdapter(this,listData);
        mListView.setAdapter(adapter);
    }

    /**
     * 更新数据
     * @param input_info
     * @return
     */
    private List<String> getNewData(String input_info){
        for (int i = 0; i < mFollowList.size(); i++) {
            String screen_name = mFollowList.get(i);
            if (screen_name.contains(input_info)) {
                screenList.add(screen_name);
            }
        }
        return screenList;
    }
}

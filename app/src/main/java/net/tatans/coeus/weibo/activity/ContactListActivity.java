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
import net.tatans.coeus.weibo.bean.ContactList;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

import java.util.ArrayList;
import java.util.List;

import static net.tatans.coeus.weibo.R.id.listView;

/**
 * Created by LCM on 2016/7/25. 13:17
 * 联系人列表ui
 */
@ContentView(R.layout.sort_activity)
public class ContactListActivity extends BaseActivity {
    //获取视图
    @ViewIoc(R.id.edt_search)
    private EditText mEdtSearch;
    @ViewIoc(listView)
    private ListView mListView;
    @ViewIoc(R.id.con_or_follow)
    private TextView con_or_follow;

    /**
     * 字体高度
     */
    List<String> list = new ArrayList<String>();
    private ContactListAdapter adapter;

    //获取用户
    private FriendshipsAPI mFriendshipsAPI;

    private Oauth2AccessToken accessToken;
    //搜索框，搜索时udpate的list
    private List<String> listString = new ArrayList<String>();

    //判断是从关注进入联系人还是从@进入
    private String mConOrFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initViewEvent();
        RequestData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mConOrFollow = getIntent().getExtras().getString(Const.CONTACT_OR_FOllOW);
        // 获取当前已保存过的 Token
        accessToken = AccessTokenKeeper.readAccessToken(this);
        //实例化关系类
        mFriendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY, accessToken);

        if (mConOrFollow.equals(Const.FOLLOW)) {
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
                String text = s.toString();
                listString.clear();
                if (mEdtSearch.getText().toString() != null) {
                    String input_info = mEdtSearch.getText().toString();
                    adapter = new ContactListAdapter(ContactListActivity.this, getNewData(input_info));
                    mListView.setAdapter(adapter);
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
        long uid = Long.parseLong(accessToken.getUid());
        mFriendshipsAPI.friends(uid, 200, 0, true, mListener);

    }

    /**
     * 填充数据并加载adapter
     *
     * @param listData
     */
    public void setListData(List<String> listData) {
        adapter = new ContactListAdapter(this, listData);
        mListView.setAdapter(adapter);
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ContactList contactList = ContactList.parse(response);
            for (int i = 0; i < contactList.contactList.size(); i++) {
                list.add(contactList.contactList.get(i).screen_name);
            }
            Log.e("mListener", "list" + list.size());
            setListData(list);
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
    private List<String> getNewData(String input_info) {
        for (int i = 0; i < list.size(); i++) {
            String screen_name = list.get(i);
            if (screen_name.contains(input_info)) {
                listString.add(screen_name);
            }
        }
        return listString;
    }
}

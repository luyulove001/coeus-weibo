package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.SearchAPI;

import net.tatans.coeus.network.tools.TatansActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.bean.UserSearchBean;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cly on 2016/7/25.
 */

public class SearchActivity extends TatansActivity {
    /**
     * 输入框
     */
    private EditText et_search;
    /**
     * 搜索按钮
     */
    private Button btn_search;
    /**
     * 显示列表，暂定有加载更多功能
     */
    private ListView ls_suggestions;
    /**
     * 请求返回处理
     */
    private RequestListener requestListener;
    /**
     * 监测输入框文字变化
     */
    private TextWatcher textWatcher;
    /**
     * 查询接口
     */
    private SearchAPI searchAPI;
    /**
     * 用户接口
     */
    private UsersAPI usersAPI;
    /**
     * 登陆验证token
     */
    private Oauth2AccessToken accessToken;
    /**
     * 用户uid
     */
    private long uid;
    /**
     * 返回的user数组
     */
    private ArrayList<UserSearchBean> users = new ArrayList<UserSearchBean>();
    /**
     * 判断是否是查询微博
     */
    private boolean isStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    private void initData() {
        isStatus = getIntent().getBooleanExtra("isStatues", true);
        requestListener = new RequestListener() {
            @Override
            public void onComplete(String s) {
                try {
                    users.clear();
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        UserSearchBean userSearchBean = UserSearchBean.parse((JSONObject) jsonArray.get(i));
                        users.add(userSearchBean);
                    }
                    ls_suggestions.setAdapter(new SimpleAdapter(SearchActivity.this, getHashMap(users),
                            android.R.layout.simple_list_item_1, new String[]{"user"}, new int[]{android.R.id.text1}));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                e.printStackTrace();
                TatansToast.showAndCancel("请求数据出错");
            }
        };
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString();
                if (!isStatus)
                    searchAPI.users(q, 10, requestListener);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        accessToken = AccessTokenKeeper.readAccessToken(this);
        uid = Long.parseLong(accessToken.getUid());
        et_search.addTextChangedListener(textWatcher);
        searchAPI = new SearchAPI(this, Constants.APP_KEY, accessToken);
        usersAPI = new UsersAPI(this, Constants.APP_KEY, accessToken);
        ls_suggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TatansToast.showAndCancel(users.get(position).getScreen_name());
                Intent i = new Intent(SearchActivity.this, MyHomePageActivity.class);
                i.putExtra("uid", users.get(position).getUid());
                startActivity(i);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                i.putExtra("content", et_search.getText().toString());
                i.setClass(SearchActivity.this, SearchResultActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * 初始化adapter的数据
     *
     * @param users 传入搜索建议用户列表
     * @return
     */
    private List<Map<String, String>> getHashMap(ArrayList<UserSearchBean> users) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> userMap;
        for (int i = 0; i < users.size(); i++) {
            userMap = new HashMap<String, String>();
            userMap.put("user", users.get(i).getScreen_name());
            list.add(userMap);
        }
        return list;
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        btn_search = (Button) findViewById(R.id.btn_search);
        ls_suggestions = (ListView) findViewById(R.id.ls_suggestion);
    }

}

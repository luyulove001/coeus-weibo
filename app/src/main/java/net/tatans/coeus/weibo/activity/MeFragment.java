package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;

import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class MeFragment extends Fragment  implements  View.OnClickListener{
    //获取视图
    private TextView myHomePage;

    private TextView mFollow;

    private TextView myFans;

    private TextView mCollection;

    private TextView mCancellation;

    private  View view ;
    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.me_page, container, false);
        initView();
        return view;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        myHomePage  = (TextView) view.findViewById(R.id.my_home_page);
        mFollow  = (TextView) view.findViewById(R.id.follow);
        myFans  = (TextView) view.findViewById(R.id.my_fans);
        mCollection  = (TextView) view.findViewById(R.id.collection);
        mCancellation  = (TextView) view.findViewById(R.id.cancellation);

        // 设置监听事件
        myHomePage.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        myFans.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mCancellation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.my_home_page:
                intent.setClass(getActivity(),MyHomePageActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.follow:
                intent.setClass(getActivity(),ContactListActivity.class);
                intent.putExtra(Const.CONTACT_OR_FOllOW,Const.FOLLOW);
                getActivity().startActivity(intent);
                break;
            case R.id.my_fans:
                intent.setClass(getActivity(),FollowersActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.collection:
                TatansToast.showAndCancel("收藏");
                break;
            case R.id.cancellation://退出登录
                new LogoutAPI(getActivity(), Constants.APP_KEY,
                        AccessTokenKeeper.readAccessToken(getActivity())).logout(mLogoutListener);
                break;
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(getActivity());
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        TatansToast.showAndCancel("已注销");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    }
}

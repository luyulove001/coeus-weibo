package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

/**
 * Created by LCM on 2016/8/12. 9:10
 * 点击某条微博菜单页
 */
@ContentView(R.layout.weibo_menu)
public class WeiboMenuDetailsActivity extends BaseActivity {
    //获取视图
    @ViewIoc(R.id.reply)
    private TextView mReply;
    @ViewIoc(R.id.write_comment)
    private TextView write_comment;
    @ViewIoc(R.id.layout_forward)
    private LinearLayout layout_forward;
    @ViewIoc(R.id.layout_comment)
    private LinearLayout layout_comment;
    @ViewIoc(R.id.layout_all_comment)
    private LinearLayout layout_all_comment;
    @ViewIoc(R.id.forward)
    private TextView forward;
    @ViewIoc(R.id.forward_num)
    private TextView forward_num;
    @ViewIoc(R.id.comment)
    private TextView comment;
    @ViewIoc(R.id.comment_num)
    private TextView comment_num;
    @ViewIoc(R.id.all_comment)
    private TextView all_comment;
    @ViewIoc(R.id.collection)
    private TextView collection;
    @ViewIoc(R.id.my_weibo_home)
    private TextView my_weibo_home;
    @ViewIoc(R.id.cancel_follow)
    private TextView cancel_follow;
    @ViewIoc(R.id.line)
    private View line;
    private String type;
    /**
     * 获取 Token
     */
    private Oauth2AccessToken accessToken;

    private StatusesAPI mStatusesAPI;

    private Long weiboId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getExtras().getString(Const.TYPE);
        weiboId = Long.valueOf(getIntent().getExtras().getString("weiboId"));
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, accessToken);
        mStatusesAPI.show(weiboId, mListener);
        if (type.equals(Const.REMIND)) {
            write_comment.setVisibility(View.GONE);
            layout_all_comment.setVisibility(View.GONE);
        } else if (type.equals(Const.MY_HOME_PAGE)) {
            mReply.setVisibility(View.GONE);
            layout_comment.setVisibility(View.GONE);
            my_weibo_home.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else if (type.equals(Const.HOME)) {
            mReply.setVisibility(View.GONE);
            layout_comment.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    /**
     * 点击回复
     */
    @OnClick(R.id.reply)
    private void onClickReply() {
        Intent intent = getIntent();
        intent.setClass(this, CommentsActivity.class);
        intent.putExtra(Const.TYPE, Const.WEIBO_COMMENT);
        startActivity(intent);
    }

    /**
     * 点击转发
     */
    @OnClick(R.id.layout_forward)
    private void onClickForward() {
        Intent intent = getIntent();
        intent.setClass(this, CommentsActivity.class);
        intent.putExtra(Const.TYPE, Const.WEIBO_FORWARD);
        startActivity(intent);
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            Log.e("adc", response + "=======");
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"created_at\"")) {
                    // response.startsWith("{\"created_at\"") 主要是判断开头字段是created_at还是statuses
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    Status status = Status.parse(response);
                    if (status != null && status.retweeted_status != null) {
                        //转发微博
                        forward_num.setText(status.retweeted_status.reposts_count + "");
                        comment_num.setText(status.retweeted_status.comments_count + "");
                    } else if(status != null){
                        //原创微博
                        forward_num.setText(status.reposts_count + "");
                        comment_num.setText(status.comments_count + "");
                    }
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            TatansToast.showAndCancel(info.toString());
        }
    };
}

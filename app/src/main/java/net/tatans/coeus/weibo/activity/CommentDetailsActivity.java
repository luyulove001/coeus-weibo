package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.AllCommentAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

/**
 * Created by LCM on 2016/8/11. 8:44
 * 评论详情
 */
@ContentView(R.layout.comment_details)
public class CommentDetailsActivity extends BaseActivity {
    @ViewIoc(R.id.all_comment_list)
    private PullToRefreshListView mPullToRefresh;
    @ViewIoc(R.id.line)
    private View line;
    @ViewIoc(R.id.line1)
    private View line1;
    @ViewIoc(R.id.line2)
    private View line2;
    @ViewIoc(R.id.write_comment)
    private LinearLayout mWriteComment;
    private Oauth2AccessToken accessToken;
    private CommentsAPI mCommentAPI;
    //查询当前微博的评论的id
    private Long weiboId;

    private CommentList comments;

    private AllCommentAdapter mAdapter;

    private boolean isFlag = true;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString(Const.COMMENT_OR_REMIND);
        weiboId = bundle.getLong(Const.WEIBO_ID);
        Log.e("weiboId",weiboId+"");
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentAPI = new CommentsAPI(this, Constants.APP_KEY, accessToken);
        mCommentAPI.show(weiboId, 0, 0, 50, 1, 0, mListener);
    }



    /**
     * 写评论
     */
    @OnClick(R.id.write_comment)
    private void onClickComment() {
        Intent intent = getIntent();
        intent.setClass(this, CommentsActivity.class);
        intent.putExtra(Const.TYPE, Const.WRITE_COMMENT);
        startActivity(intent);
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                Log.e("allcomment",response);
                comments = CommentList.parse(response);
                mAdapter = new AllCommentAdapter(CommentDetailsActivity.this, comments);
                mPullToRefresh.setAdapter(mAdapter);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            TatansToast.showAndCancel(info.toString());
        }
    };

}

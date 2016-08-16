package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    //获取视图
    @ViewIoc(R.id.tv_reply)
    private TextView mReply;
    @ViewIoc(R.id.layout_all_comment)
    private LinearLayout mAllComment;
    @ViewIoc(R.id.comment_num)
    private TextView mCommentNum;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        weiboId = Long.valueOf(getIntent().getExtras().getString("weiboId"));
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentAPI = new CommentsAPI(this, Constants.APP_KEY, accessToken);
        mCommentAPI.show(weiboId, 0, 0, 50, 1, 0, mListener);
        mWriteComment.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
    }

    /**
     * 回复评论
     */
    @OnClick(R.id.tv_reply)
    private void onClickReply() {
        Intent intent = getIntent();
        intent.setClass(this, CommentsActivity.class);
        intent.putExtra(Const.TYPE, Const.REPLY);
        startActivity(intent);
    }

    /**
     * 点击查看所有微博的评论
     */
    @OnClick(R.id.layout_all_comment)
    private void onClickAllComment() {
        if (comments != null) {
            isFlag = false;
            mWriteComment.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
            mReply.setVisibility(View.GONE);
            mAllComment.setVisibility(View.GONE);
            mPullToRefresh.setVisibility(View.VISIBLE);
            mAdapter = new AllCommentAdapter(this, comments);
            mPullToRefresh.setAdapter(mAdapter);
        } else {
            TatansToast.showAndCancel("正在加载数据");
        }


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
                comments = CommentList.parse(response);
                mCommentNum.setText(comments.commentList.size() + "");
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            TatansToast.showAndCancel(info.toString());
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !isFlag) {
            moveTaskToBack(false);//true对任何Activity都适用
            mWriteComment.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            line.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            mReply.setVisibility(View.VISIBLE);
            mAllComment.setVisibility(View.VISIBLE);
            mPullToRefresh.setVisibility(View.GONE);
            isFlag = true;
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

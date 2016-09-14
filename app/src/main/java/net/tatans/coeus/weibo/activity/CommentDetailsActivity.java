package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
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
import net.tatans.coeus.weibo.adapter.StatusAdapter;
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
    private boolean isRefresh = false, isEnd = false;
    private int index = 1;//当前页数

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
        comments = new CommentList();
        type = bundle.getString(Const.COMMENT_OR_REMIND);
        weiboId = bundle.getLong(Const.WEIBO_ID);
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentAPI = new CommentsAPI(this, Constants.APP_KEY, accessToken);
        mCommentAPI.show(weiboId, 0, 0, 50, index, 0, mListener);
        mPullToRefresh.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (isEnd) {
                    TatansToast.showAndCancel("没有更多内容了");
                    return;
                }
                isRefresh = false;
                index += 1;
                mCommentAPI.show(weiboId, 0, 0, 50, index, 0, mListener);
            }
        });
        mPullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(CommentDetailsActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                mPullToRefresh.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                index = 1;
                isRefresh = true;
                isEnd = false;
                comments.commentList.clear();
                mPullToRefresh.setRefreshing();
                mCommentAPI.show(weiboId, 0, 0, 50, index, 0, mListener);
            }
        });
    }


    /**
     * 写评论
     */
    @OnClick(R.id.write_comment)
    private void onClickComment() {
        Intent intent = getIntent();
        intent.setClass(this, CommentsActivity.class);
        intent.putExtra(Const.TYPE, Const.WRITE_COMMENT);
        intent.putExtra("weiboId", String.valueOf(weiboId));
        startActivity(intent);
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                Log.e("allcomment", response);
                mPullToRefresh.onRefreshComplete();
                CommentList comments1 = CommentList.parse(response);
                if (comments1.commentList == null || comments1.commentList.isEmpty()) {
                    isEnd = true;
                    TatansToast.showAndCancel("暂无评论");
                    return;
                }
                if (comments1.commentList.size() < 50)
                    isEnd = true;
                if (comments.commentList == null || comments.commentList.isEmpty() || isRefresh) {//刷新
                    comments = comments1;
                    mAdapter = new AllCommentAdapter(CommentDetailsActivity.this, comments);
                    mPullToRefresh.setAdapter(mAdapter);
                } else {//加载
                    comments.commentList.addAll(comments1.commentList);
                    mAdapter.notifyDataSetChanged();
                    TatansToast.showAndCancel("加载第" + index + "页");
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

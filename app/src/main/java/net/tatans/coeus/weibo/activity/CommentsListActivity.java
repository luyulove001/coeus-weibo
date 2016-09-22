package net.tatans.coeus.weibo.activity;


import android.os.Bundle;
import android.widget.ListView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.CommentAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

/**
 * Created by LCM on 2016/7/27. 13:18
 * 评论列表
 */
@ContentView(R.layout.comments)
public class CommentsListActivity extends BaseActivity {
    //获取视图
    @ViewIoc(R.id.comments_list)
    private ListView mCommentsList;

    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 微博评论接口
     */
    private CommentsAPI mCommentsAPI;

    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        RequestData();
    }

    /**
     * 初始化
     */
    private void initData() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, mAccessToken);
    }

    /**
     * 请求数据
     */
    private void RequestData() {
        long uid = Long.parseLong(mAccessToken.getUid());
        mCommentsAPI.toME(uid, 0, 50, 1, 0, 0, mListener);
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            CommentList comment = CommentList.parse(response);
            if (comment.commentList == null) {
                TatansToast.showAndCancel("未请求到数据");
                return;
            }
            adapter = new CommentAdapter(CommentsListActivity.this, comment);
            mCommentsList.setAdapter(adapter);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            TatansToast.showAndCancel(info.toString());
        }
    };


}

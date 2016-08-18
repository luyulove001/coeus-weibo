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
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Favorite;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.bean.FollowBean;
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
    @ViewIoc(R.id.all_comment_num)
    private TextView all_comment_num;
    @ViewIoc(R.id.collection)
    private TextView collection;
    @ViewIoc(R.id.my_weibo_home)
    private TextView my_weibo_home;
    @ViewIoc(R.id.cancel_follow)
    private TextView cancel_follow;
    @ViewIoc(R.id.line)
    private View line;
    @ViewIoc(R.id.line1)
    private View line1;
    @ViewIoc(R.id.line6)
    private View line6;
    @ViewIoc(R.id.line5)
    private View line5;
    @ViewIoc(R.id.line4)
    private View line4;
    @ViewIoc(R.id.line3)
    private View line3;
    @ViewIoc(R.id.follow_blogger)
    private TextView follow_blogger;

    private String type;
    /**
     * 获取 Token
     */
    private Oauth2AccessToken accessToken;
    //收藏接口
    private FavoritesAPI mFavoritesAPI;
    //关系接口
    private FriendshipsAPI mFriendshipsAPI;

    private CommentsAPI mCommentAPI;
    private Long weiboId;
    private Long uid;
    private String screen_name;
    //是否关注某用户
    private boolean isFollow = false;
    //是否收藏某条微博
    private boolean isFavorites = false;
    //评论数
    private int comments_count;
    //转发数
    private int reposts_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getExtras().getString(Const.TYPE);
        weiboId = Long.valueOf(getIntent().getExtras().getString(Const.WEIBO_ID));
        uid = Long.valueOf(getIntent().getExtras().getString(Const.UID));
        screen_name = getIntent().getExtras().getString(Const.SCREEN_NAME);
        comments_count = getIntent().getExtras().getInt(Const.COMMENTS_COUNT);
        reposts_count = getIntent().getExtras().getInt(Const.REPOSTS_COUNT);
        isFavorites = getIntent().getExtras().getBoolean(Const.FAVORITES);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mFavoritesAPI = new FavoritesAPI(this, Constants.APP_KEY, accessToken);
        mFriendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY, accessToken);
        if(type.equals(Const.COMMENT)){
            Log.e("TAG",")))COMMENT"+Const.COMMENT);
            mCommentAPI = new CommentsAPI(this, Constants.APP_KEY, accessToken);
            mCommentAPI.show(weiboId, 0, 0, 50, 1, 0, mListener);
        }
        //用于判断当前用户是否关注了某用户
        mFriendshipsAPI.show(Long.parseLong(accessToken.getUid()), uid, mListener);
        ViweDisplay();
    }

    /**
     * ui界面的一些处理
     */
    private void ViweDisplay() {
        if (type.equals(Const.REMIND)) {
            write_comment.setVisibility(View.GONE);
            layout_all_comment.setVisibility(View.GONE);
            follow_blogger.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        } else if (type.equals(Const.MY_HOME_PAGE)) {
            mReply.setVisibility(View.GONE);
            layout_comment.setVisibility(View.GONE);
            my_weibo_home.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            follow_blogger.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        } else if (type.equals(Const.HOME)) {
            mReply.setVisibility(View.GONE);
            layout_comment.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            follow_blogger.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        } else if (type.equals(Const.WEIBO_FAVORITE)) {
            mReply.setVisibility(View.GONE);
            layout_comment.setVisibility(View.GONE);
            cancel_follow.setVisibility(View.GONE);
        }else if(type.equals(Const.COMMENT)){
            follow_blogger.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
            layout_forward.setVisibility(View.GONE);
            write_comment.setVisibility(View.GONE);
            layout_comment.setVisibility(View.GONE);
            collection.setVisibility(View.GONE);
            my_weibo_home.setVisibility(View.GONE);
            cancel_follow.setVisibility(View.GONE);
            line6.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            line5.setVisibility(View.GONE);
        }
        comment_num.setText(comments_count + "");
        forward_num.setText(reposts_count + "");
        if (!type.equals(Const.COMMENT)) {
            all_comment_num.setText(comments_count + "");
        }
        if (isFavorites) {
            collection.setText("取消收藏");
        } else {
            collection.setText("收藏");
        }
    }

    /**
     * 点击回复
     */
    @OnClick(R.id.reply)
    private void onClickReply() {
        Intent intent = getIntent();
        intent.setClass(this, CommentsActivity.class);
        intent.putExtra(Const.TYPE, Const.WRITE_COMMENT);
        intent.putExtra("isReply", true);
        if(Const.TYPE.equals(Const.COMMENT)){
            intent.putExtra(Const.TYPE, Const.REPLY);
        }else{
            intent.putExtra(Const.TYPE, Const.WEIBO_COMMENT);
        }
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
     * 点击评论
     */
    @OnClick(R.id.layout_comment)
    private void onClickComment() {
        Intent intent = new Intent(this, CommentDetailsActivity.class);
        intent.putExtra(Const.WEIBO_ID, weiboId);
        intent.putExtra(Const.COMMENT_OR_REMIND, Const.REMIND);
        startActivity(intent);
    }

    /**
     * 点击写评论
     */
    @OnClick(R.id.write_comment)
    private void onClickWriteComment() {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(Const.TYPE, Const.WRITE_COMMENT);
        intent.putExtra("weiboId", weiboId + "");
        startActivity(intent);
    }

    /**
     * 点击收藏，收藏一条微博
     */
    @OnClick(R.id.collection)
    private void onClickFavorites() {
        if (isFavorites) {
            mFavoritesAPI.destroy(weiboId, mListener);
            TatansToast.showAndCancel("取消成功");
        } else {
            mFavoritesAPI.create(weiboId, mListener);
            TatansToast.showAndCancel("收藏成功");

        }
    }

    /**
     * 点击进入博主主页
     */
    @OnClick(R.id.my_weibo_home)
    private void onClickHome() {
        Intent intent = new Intent(this, MyHomePageActivity.class);
        intent.putExtra(Const.UID, uid);
        startActivity(intent);
    }

    /**
     * 关注某用户
     */
    @OnClick(R.id.cancel_follow)
    private void onClickFriendships() {
        if (isFollow) {
            cancel_follow.setText("关注该博主");
            mFriendshipsAPI.destroy(uid, screen_name, mListener);
        } else {
            cancel_follow.setText("取消关注");
            mFriendshipsAPI.create(uid, screen_name, mListener);
        }
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"status\"")) {
                    Favorite favorite = Favorite.parse(response);
                    isFavorites = favorite.status.favorited;
                    if (isFavorites) {
                        collection.setText("取消收藏");
                    } else {
                        collection.setText("收藏");
                    }
                } else if (response.startsWith("{\"id\"")) {
                    if (isFollow) {
                        TatansToast.showAndCancel("取消成功");
                    } else {
                        TatansToast.showAndCancel("关注成功");
                    }
                } else if(response.startsWith("{\"comments\"")){
                    CommentList commentList = CommentList.parse(response);
                    if(commentList!=null){
                        all_comment_num.setText(commentList.commentList.size()+"");
                    }
                }else{
                    //解析是否关注该用户
                    FollowBean followBean = FollowBean.parse(response);
                    isFollow = followBean.following;
                    if (isFollow) {
                        cancel_follow.setText("取消关注");
                    } else {
                        cancel_follow.setText("关注该博主");
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

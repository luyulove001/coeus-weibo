package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.models.FavoriteList;

import net.tatans.coeus.network.tools.TatansActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.view.ViewInject;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.FavoritesAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;

/**
 * Created by LCM on 2016/8/5. 13:11
 * 收藏界面
 */
public class FavoritesActivity extends TatansActivity {
    @ViewInject(id = R.id.home_page_listview)
    private PullToRefreshListView mPullToRefresh;
    private Oauth2AccessToken accessToken;
    private FavoritesAPI mFavoritesApi;
    private FavoritesAdapter mFavorites;
    private boolean isEnd = false;
    private int index = 1;
    private boolean isRefresh = false;
    private FavoriteList favoriteList = new FavoriteList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoriters);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mFavoritesApi = new FavoritesAPI(this, Constants.APP_KEY, accessToken);
        mFavoritesApi.favorites(50, 1, mListener);
        //下拉刷新
        mPullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                mFavoritesApi.favorites(50, 1, mListener);
                mPullToRefresh.setRefreshing();
                isRefresh = true;
                isEnd = false;
                index = 1;
            }
        });
        //上拉加载
        mPullToRefresh.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!isEnd) {
                    index += 1;
                    mFavoritesApi.favorites(50, index, mListener);
                    TatansToast.showAndCancel("加载第" + index + "页");
                    isRefresh = false;
                } else {
                    TatansToast.showAndCancel("没有更多内容了");
                }
            }
        });

    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            FavoriteList favorite = FavoriteList.parse(response);
            if (favorite.favoriteList == null || favorite.favoriteList.isEmpty()) {
                isEnd = true;
                return;
            }
            if (favoriteList.favoriteList == null || favoriteList.favoriteList.isEmpty() || isRefresh) {
                favoriteList = favorite;
                mFavorites = new FavoritesAdapter(FavoritesActivity.this, favoriteList);
                mPullToRefresh.setAdapter(mFavorites);
                mPullToRefresh.onRefreshComplete();
            } else {
                favoriteList.favoriteList.addAll(favorite.favoriteList);
                mFavorites.notifyDataSetChanged();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };
}

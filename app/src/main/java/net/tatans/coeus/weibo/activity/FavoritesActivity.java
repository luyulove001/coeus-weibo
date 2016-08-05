package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.models.FavoriteList;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.FavoritesAdapter;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LCM on 2016/8/5. 13:11
 * 收藏界面
 */
@ContentView(R.layout.favoriters)
public class FavoritesActivity extends BaseActivity {
    @ViewIoc(R.id.home_page_listview)
    private PullToRefreshListView mPullToRefresh;

    private Oauth2AccessToken accessToken;

    private FavoritesAPI mFavoritesApi;
    private FavoritesAdapter mFavorites;
    List<FavoriteList> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        accessToken = AccessTokenKeeper.readAccessToken(this);
        mFavoritesApi = new FavoritesAPI(this, Constants.APP_KEY, accessToken);
        mFavoritesApi.favorites(50, 1, mListener);
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            FavoriteList favorite = FavoriteList.parse(response);
            Log.e("response", "response::" +  favorite.favoriteList.size());
            favoriteList = new ArrayList<FavoriteList>();
            favoriteList.add(favorite);
            mFavorites = new FavoritesAdapter(FavoritesActivity.this, favoriteList);
            mPullToRefresh.setAdapter(mFavorites);
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };
}

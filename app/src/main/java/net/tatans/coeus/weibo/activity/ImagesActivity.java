package net.tatans.coeus.weibo.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansBitmap;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.ViewPagerAdapter;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

import java.util.ArrayList;

/**
 * Created by LCM on 2016/8/8. 10:33
 * 显示图片的activity
 */
@ContentView(R.layout.images)
public class ImagesActivity extends BaseActivity {
    //存放图片链接
    private ArrayList<String> pic_urls;
    @ViewIoc(R.id.images_view_page)
    private ViewPager viewPager;

    private ViewPagerAdapter adapter;
    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("图片");
        //得到图片链接的集合
        pic_urls = getIntent().getExtras().getStringArrayList(Const.PICURLS);
        initData();
    }

    private void initData() {
        mImageViews = new ImageView[pic_urls.size()];
        for (int i = 0; i < pic_urls.size(); i++) {
            ImageView image = new ImageView(ImagesActivity.this);
            //将url中的thumbnail替换成large，即将缩略图替换成高清图 large
            String url = pic_urls.get(i).replace(pic_urls.get(i).substring(22, pic_urls.get(i).lastIndexOf("/")), "large");
            TatansBitmap tb = null;
            tb = TatansBitmap.create();
            tb.display(image, url);
            mImageViews[i] = image;
        }
        mHandler.sendEmptyMessage(1);

    }

    /**
     * 更新ui
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new ViewPagerAdapter(ImagesActivity.this, mImageViews);
            viewPager.setAdapter(adapter);
        }
    };
}

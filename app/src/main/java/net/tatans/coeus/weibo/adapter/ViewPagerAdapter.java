package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by LCM on 2016/8/8. 13:20
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<Bitmap> mListView;
    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    public ViewPagerAdapter(Context context, ImageView[] listView) {
        this.mContext = context;
        this.mImageViews = listView;
    }

    @Override
    public int getCount() {
        return mImageViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {

        return view == o;
    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView(mImageViews[position % mImageViews.length], 0);
        return mImageViews[position % mImageViews.length];
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);
    }
}

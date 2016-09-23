package net.tatans.coeus.weibo.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.tatans.coeus.network.tools.TatansActivity;
import net.tatans.coeus.network.tools.TatansBitmap;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.view.ViewInject;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.ViewPagerAdapter;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by LCM on 2016/8/8. 10:33
 * 显示图片的activity
 */
public class ImagesActivity extends TatansActivity implements PlatformActionListener {
    //存放图片链接
    private ArrayList<String> pic_urls;
    @ViewInject(id = R.id.images_view_page)
    private ViewPager viewPager;
    @ViewInject(id = R.id.search, click = "onClickShare")
    private LinearLayout search;
    private ViewPagerAdapter adapter;
    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;


    private GestureDetector mGD;

    private int PageCount;//共几张图片

    private int iCurrentPage;//当前第几页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images);
        setTitle("第一张");
        //得到图片链接的集合
        pic_urls = getIntent().getExtras().getStringArrayList(Const.PICURLS);
        initData();
    }

    private void initData() {
        ShareSDK.initSDK(this);
        mGD = new GestureDetector(this, new myOnGestureListener());
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
     * 点击分享
     */
    private void onClickShare() {
        showShareDialog();
    }


    /**
     * 显示分享
     */
    ShareDialog shareDialog;

    private void showShareDialog() {
        shareDialog = new ShareDialog(this);
        shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });
        shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                //得到当前显示图片的url
                String url = pic_urls.get(iCurrentPage).replace(pic_urls.get(iCurrentPage).substring(22, pic_urls.get(iCurrentPage).lastIndexOf("/")), "large");
                Platform.ShareParams sp = new Platform.ShareParams();
                if (item.get("ItemText").equals("到微信")) {
                    //设置分享内容
                    sp.setShareType(Platform.SHARE_IMAGE);//非常重要：一定要设置分享属性
                    sp.setImageUrl(url);
                    //非常重要：获取平台对象
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(ImagesActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechat.share(sp);

                } else if (item.get("ItemText").equals("到微信朋友圈")) {
                    //设置分享内容
                    sp.setShareType(Platform.SHARE_IMAGE); //非常重要：一定要设置分享属性
                    sp.setImageUrl(url);
                    //非常重要：获取平台对象
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(ImagesActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechatMoments.share(sp);
                } else if (item.get("ItemText").equals("到QQ")) {
                    //设置分享内容
                    sp.setImageUrl(url);

                    //非常重要：获取平台对象
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(ImagesActivity.this); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);
                } else if (item.get("ItemText").equals("到QQ空间")) {
                    //设置分享内容
                    sp.setImageUrl(url);
                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(ImagesActivity.this); // 设置分享事件回调
                    // 执行分享
                    qzone.share(sp);
                }
                shareDialog.dismiss();
            }
        });
    }


    /**
     * 回调的地方是子线程，进行UI操作要用handle处理
     */
    @Override
    public void onCancel(Platform arg0, int arg1) {
        shareHandler.sendEmptyMessage(6);
    }

    /**
     * 回调的地方是子线程，进行UI操作要用handle处理
     */
    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        /**判断成功的平台是不是新浪微博*/
        if (arg0.getName().equals(Wechat.NAME)) {
            shareHandler.sendEmptyMessage(2);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            shareHandler.sendEmptyMessage(3);
        } else if (arg0.getName().equals(QQ.NAME)) {
            shareHandler.sendEmptyMessage(4);
        } else if (arg0.getName().equals(QZone.NAME)) {
            shareHandler.sendEmptyMessage(5);
        }
    }

    /**
     * 回调的地方是子线程，进行UI操作要用handle处理
     */
    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 7;
        msg.obj = arg2.getMessage();
        shareHandler.sendMessage(msg);
    }

    Handler shareHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    TatansToast.showAndCancel("微信分享成功");
                    break;
                case 3:
                    TatansToast.showAndCancel("朋友圈分享成功");
                    break;
                case 4:
                    TatansToast.showAndCancel("QQ分享成功");
                    break;
                case 5:
                    TatansToast.showAndCancel("QQ空间分享成功");
                    break;
                case 6:
                    TatansToast.showAndCancel("取消分享");
                    break;
                case 7:
                    if (msg.obj == null) {
                        TatansToast.showAndCancel("分享失败，可能未安装最新版本的QQ");
                    } else if (msg.obj.toString().contains("400")) {
                        TatansToast.showAndCancel("相同内容短时间内不可重复分享");
                    } else {
                        TatansToast.showAndCancel("分享失败");
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 更新ui
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new ViewPagerAdapter(ImagesActivity.this, mImageViews);
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(new MyListener());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    /**
     * 当第一张图片切换到最后一张图片时，可以实现快速播报，以及获取当前页的内容
     */
    private class myOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() < -120 && (iCurrentPage + 1) == 1) {
                TatansToast.showAndCancel("第" + (iCurrentPage + 1) + "张");
            } else if (e1.getX() - e2.getX() > 60 && iCurrentPage != PageCount - 1) {
                viewPager.setCurrentItem(iCurrentPage + 1);
            } else if (e1.getX() - e2.getX() < -60 && iCurrentPage != 0) {
                viewPager.setCurrentItem(iCurrentPage - 1);
            }
            return false;
        }
    }

    /**
     * 手势分发
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mGD != null) {
            if (mGD.onTouchEvent(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            iCurrentPage = position;
            if (position == 0 || position == PageCount - 1) {
                viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mGD.onTouchEvent(event);
                    }
                });
            }
            TatansToast.cancel();
            if (position < PageCount - 1) {
                TatansToast.showAndCancel("第" + (position + 1) + "张");
            } else {
                TatansToast.showAndCancel("第" + (position + 1) + "张");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

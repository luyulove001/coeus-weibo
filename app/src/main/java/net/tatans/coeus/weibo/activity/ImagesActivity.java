package net.tatans.coeus.weibo.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansBitmap;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.ViewPagerAdapter;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.ShareDialog;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

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
@ContentView(R.layout.images)
public class ImagesActivity extends BaseActivity implements PlatformActionListener {
    //存放图片链接
    private ArrayList<String> pic_urls;
    @ViewIoc(R.id.images_view_page)
    private ViewPager viewPager;
    @ViewIoc(R.id.search)
    private LinearLayout search;
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
        ShareSDK.initSDK(this);
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
    @OnClick(R.id.search)
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
                if (item.get("ItemText").equals("到微信")) {
                    //设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_IMAGE);//非常重要：一定要设置分享属性
                    sp.setImageUrl( pic_urls.get(0).replace(pic_urls.get(0).substring(22, pic_urls.get(0).lastIndexOf("/")), "large"));
                    //非常重要：获取平台对象
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(ImagesActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechat.share(sp);

                } else if (item.get("ItemText").equals("到微信朋友圈")) {
                    //设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_IMAGE); //非常重要：一定要设置分享属性
                    sp.setImageUrl( pic_urls.get(0).replace(pic_urls.get(0).substring(22, pic_urls.get(0).lastIndexOf("/")), "large"));
                    //非常重要：获取平台对象
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(ImagesActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechatMoments.share(sp);
                } else if (item.get("ItemText").equals("到QQ")) {
                    //设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setImageUrl( pic_urls.get(0).replace(pic_urls.get(0).substring(22, pic_urls.get(0).lastIndexOf("/")), "large"));

                    //非常重要：获取平台对象
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(ImagesActivity.this); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);
                } else if (item.get("ItemText").equals("到QQ空间")) {
                    //设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setImageUrl( pic_urls.get(0).replace(pic_urls.get(0).substring(22, pic_urls.get(0).lastIndexOf("/")), "large"));
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
                case 1:
                    TatansToast.showAndCancel("新浪微博分享成功");
                    break;
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
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }
}

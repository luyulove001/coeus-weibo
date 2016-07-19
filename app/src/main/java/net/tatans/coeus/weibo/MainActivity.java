package net.tatans.coeus.weibo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import net.tatans.coeus.network.tools.BaseActivity;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // 获取界面的视图
    private LinearLayout id_tab_home;
    private LinearLayout id_tab_message;
    private LinearLayout id_tab_search;
    private LinearLayout id_tab_me;

    private ImageButton id_tab_home_img;
    private ImageButton id_tab_message_img;
    private ImageButton id_tab_search_img;
    private ImageButton id_tab_me_img;

    private Fragment mTab01;
    private Fragment mTab02;
    private Fragment mTab03;
    private Fragment mTab04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        setSelect(0);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        id_tab_home = (LinearLayout) findViewById(R.id.id_tab_home);
        id_tab_message = (LinearLayout) findViewById(R.id.id_tab_message);
        id_tab_search = (LinearLayout) findViewById(R.id.id_tab_search);
        id_tab_me = (LinearLayout) findViewById(R.id.id_tab_me);

        id_tab_home_img = (ImageButton) findViewById(R.id.id_tab_home_img);
        id_tab_message_img = (ImageButton) findViewById(R.id.id_tab_message_img);
        id_tab_search_img = (ImageButton) findViewById(R.id.id_tab_search_img);
        id_tab_me_img = (ImageButton) findViewById(R.id.id_tab_me_img);

        // 设置button的监听事件
        id_tab_home.setOnClickListener(this);
        id_tab_message.setOnClickListener(this);
        id_tab_search.setOnClickListener(this);
        id_tab_me.setOnClickListener(this);
    }

    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                if (mTab01 == null) {
                    mTab01 = new HomeFragment();
                    transaction.add(R.id.id_content, mTab01);
                } else {
                    transaction.show(mTab01);
                }
                break;
            case 1:
                if (mTab02 == null) {
                    mTab02 = new MessageFragment();
                    transaction.add(R.id.id_content, mTab02);
                } else {
                    transaction.show(mTab02);
                }
                break;
            case 2:
                if (mTab03 == null) {
                    mTab03 = new FindFragment();
                    transaction.add(R.id.id_content, mTab03);
                } else {
                    transaction.show(mTab03);
                }
                break;
            case 3:
                if (mTab04 == null) {
                    mTab04 = new MeFragment();
                    transaction.add(R.id.id_content, mTab04);
                } else {
                    transaction.show(mTab04);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏掉不是显示tab的其他内容
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        }
        if (mTab04 != null) {
            transaction.hide(mTab04);
        }
    }

    @Override
    public void onClick(View v) {
//        resetImgs();
        switch (v.getId()) {
            case R.id.id_tab_home:
                setSelect(0);
                break;
            case R.id.id_tab_message:
                setSelect(1);
                break;
            case R.id.id_tab_search:
                setSelect(2);
                break;
            case R.id.id_tab_me:
                setSelect(3);
                break;

            default:
                break;
        }
    }

    /**
     * 切换图片至暗色
     */
//    private void resetImgs() {
//        id_tab_weixin_img.setImageResource(R.drawable.tab_weixin_normal);
//        id_tab_frd_img.setImageResource(R.drawable.tab_find_frd_normal);
//        id_tab_address_img.setImageResource(R.drawable.tab_address_normal);
//        id_tab_settings_img.setImageResource(R.drawable.tab_settings_normal);
//    }
}

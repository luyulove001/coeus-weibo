package net.tatans.coeus.weibo.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import net.tatans.coeus.weibo.R;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // 获取界面的视图
    private LinearLayout id_tab_home;
    private LinearLayout id_tab_message;
    private LinearLayout id_tab_search;
    private LinearLayout id_tab_me;

    private Fragment mHomeFragment;
    private Fragment mMessageFragment;
    private Fragment mFindFragment;
    private Fragment mMeFragment;
    private Handler handler;
    private int click_judgment = 1;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

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
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.id_content, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                id_tab_home.setBackgroundColor(getResources().getColor(R.color.colorBottomBright));
                break;
            case 1:
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    transaction.add(R.id.id_content, mMessageFragment);
                } else {
                    transaction.show(mMessageFragment);
                }
                id_tab_message.setBackgroundColor(getResources().getColor(R.color.colorBottomBright));
                break;
            case 2:
                if (mFindFragment == null) {
                    mFindFragment = new FindFragment();
                    transaction.add(R.id.id_content, mFindFragment);
                } else {
                    transaction.show(mFindFragment);
                }
                id_tab_search.setBackgroundColor(getResources().getColor(R.color.colorBottomBright));
                break;
            case 3:
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                    transaction.add(R.id.id_content, mMeFragment);
                } else {
                    transaction.show(mMeFragment);
                }
                id_tab_me.setBackgroundColor(getResources().getColor(R.color.colorBottomBright));
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
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }
        if (mFindFragment != null) {
            transaction.hide(mFindFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    @Override
    public void onClick(View v) {
        resetImgs();
        switch (v.getId()) {
            case R.id.id_tab_home:
                setSelect(0);
                click_judgment += 1;
                if (click_judgment > 1) {
                    handler.sendEmptyMessage(2);
                }

                break;
            case R.id.id_tab_message:
                setSelect(1);
                click_judgment = 0;
                break;
            case R.id.id_tab_search:
                setSelect(2);
                click_judgment = 0;
                break;
            case R.id.id_tab_me:
                setSelect(3);
                click_judgment = 0;
                break;

            default:
                break;
        }
    }

    /**
     * 切换图片至暗色
     */
    private void resetImgs() {
        id_tab_home.setBackgroundColor(getResources().getColor(R.color.colorBottomDark));
        id_tab_message.setBackgroundColor(getResources().getColor(R.color.colorBottomDark));
        id_tab_search.setBackgroundColor(getResources().getColor(R.color.colorBottomDark));
        id_tab_me.setBackgroundColor(getResources().getColor(R.color.colorBottomDark));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        click_judgment = 0;
    }
}

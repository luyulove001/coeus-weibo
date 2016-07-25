package net.tatans.coeus.weibo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.adapter.AppListAdapter;
import net.tatans.coeus.weibo.bean.Person;
import net.tatans.coeus.weibo.model.imp.ISendChar;
import net.tatans.coeus.weibo.model.imp.ITatansItemClick;
import net.tatans.coeus.weibo.util.FavoriteComparator;
import net.tatans.coeus.weibo.util.PinyinComparator;
import net.tatans.coeus.weibo.util.StringHelper;
import net.tatans.coeus.weibo.view.NewTextView;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.tatans.coeus.weibo.R.id.listView;

/**
 * Created by LCM on 2016/7/25. 13:17
 * 联系人列表ui
 */
@ContentView(R.layout.sort_activity)
public class AppListActivity extends BaseActivity implements ITatansItemClick, ISendChar {
    //获取视图
    @ViewIoc(R.id.edt_search)
    private EditText mEdtSearch;
    @ViewIoc(listView)
    private ListView mListView;
    @ViewIoc(R.id.layout)
    private LinearLayout mLayout;
    @ViewIoc(R.id.tv)
    private TextView mShow;

    private String[] indexStr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};

    private static int HEIGHT;
    /**
     * 字体高度
     */
    private boolean flag;
    List<String> list = new ArrayList<String>();
    private List<Person> newPersons;
    private List<Person> mSortList;
    //测试使用的bean
    String[] stringName = {"阿妹", "阿郎", "陈奕迅", "周杰伦", "曾一鸣", "成龙", "王力宏", "汪峰", "王菲", "那英",
            "张伟", "张学友", "李德华", "郑源", "白水水", "白天不亮", "陈龙", "陈丽丽", "哈林", "高进", "高雷",
            "阮今天", "龚琳娜", "苏醒", "苏永康", "陶喆", "沙宝亮", "宋冬野", "宋伟", "袁成杰", "戚薇", "齐大友",
            "齐天大圣", "品冠", "吴克群", "BOBO", "Jobs", "动力火车", "伍佰", "#蔡依林", "$797835344$", "Jack", "~夏先生"};
    private AppListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initViewEvent();
        for(int i=0;i<stringName.length;i++){
            String name = stringName[i];
            list.add(name);
        }
        Log.e("list",list.get(0)+list.size());
        setListData(list);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        newPersons = new ArrayList<Person>();
        mSortList = new ArrayList<Person>();
    }

    /**
     * 初始化布局数据
     */
    private void initViewEvent() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() == 1) {
                    onChangeText(StringHelper.getPinYinHeadChar(text));
                } else if (text.length() > 1) {
                    onChangeText(StringHelper.getPinYinHeadChar(text.substring(0, 1)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 填充数据并加载adapter
     *
     * @param listData
     */
    public void setListData(List<String> listData) {
        newPersons.clear();
        newPersons = setFilledData(listData);
        // 根据a-z进行排序源数据
        Collections.sort(newPersons, new PinyinComparator());
        Collections.sort(newPersons, new FavoriteComparator());
        adapter = new AppListAdapter(this, newPersons, this);
        mListView.setAdapter(adapter);
    }

    /**
     * 为Person填充数据
     *
     * @param date
     * @return
     */
    private List<Person> setFilledData(List<String> date) {
        for (int i = 0; i < date.size(); i++) {
            Person sortModel = new Person();
            sortModel.setName(date.get(i).toString());
            // 汉字转换成拼音
            // 正则表达式，判断首字母是否是英文字母
            //汉字转换成拼音
            String sortString = StringHelper.getPinYinHeadChar(date.get(i).toString().substring(0, 1));
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[a-zA-Z]")) {
                sortModel.setPinYinName(sortString.toUpperCase());
            } else {
                sortModel.setPinYinName("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    @Override
    public void onSendChar(String sChar) {
        onChangeText(sChar);
    }

    @Override
    public void OnTatansItemClick(int code, String name) {

    }

    @Override
    public void OnTatansItemLongClick(int code, String name) {

    }

    /**
     * 根据text(ABCDE...)刷新listview列表
     *
     * @param text
     */

    private void onChangeText(String text) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(text.charAt(0));
        if (position != -1) {
            mListView.setSelection(position);
        }
    }


    /**
     * 根据屏幕高度绘制索引
     * 在onCreate里面得到的getHeight=0，So，在onWindowFocusChanged得到getHeight
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!flag) {
            HEIGHT = (mLayout.getMeasuredHeight() - 40) / indexStr.length;
            setIndexSideView();
            flag = true;
        }
    }


    /**
     * 绘制侧栏索引列表
     */
    private void setIndexSideView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, HEIGHT);
        for (int i = 0; i < indexStr.length; i++) {
            final NewTextView tv_label = new NewTextView(this, this);
            if (i == 0) tv_label.setContentDescription("收藏。单指快速左右滑动来滚动翻页");
            else if (i == indexStr.length - 1) tv_label.setContentDescription("其他。单指快速左右滑动来滚动翻页");
            else tv_label.setContentDescription(" " + indexStr[i] + "。单指快速左右滑动来滚动翻页");
            tv_label.setGravity(Gravity.CENTER);
            tv_label.setLayoutParams(params);
            tv_label.setText(indexStr[i]);
            tv_label.setTextColor(getResources().getColor(android.R.color.white));
            tv_label.setPadding(15, 0, 15, 0);
            mLayout.addView(tv_label);
            tv_label.setTextSize(11.5f);
            /** 该方法只有在所有的无障碍服务都关闭才有效 */
            if (tv_label.isOk()) {
                //侧边栏的OnTouchListener
                mLayout.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        float y = event.getY();
                        int index = (int) (y / HEIGHT);
                        if (index > -1 && index < indexStr.length) {// 防止越界
                            String key = indexStr[index];
                            for (int i = 0; i < mSortList.size(); i++) {
                                if (mSortList.get(i).getPinYinName().equals(key)) {
                                    if (mListView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
                                        mListView.setSelectionFromTop(i + mListView.getHeaderViewsCount(), 0);
                                    } else {
                                        mListView.setSelectionFromTop(i, 0);// 滑动到第一项
                                    }
                                    mShow.setVisibility(View.VISIBLE);
                                    mShow.setText(indexStr[index]);
                                    break;/**执行一次就好*/
                                }
                            }
                        }
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mListView.setBackgroundColor(Color.parseColor("#151b21"));
                                break;

                            case MotionEvent.ACTION_MOVE:

                                break;
                            case MotionEvent.ACTION_UP:
                                mListView.setBackgroundColor(Color.parseColor("#00ffffff"));
                                mShow.setVisibility(View.GONE);
                                break;
                        }
                        return true;
                    }
                });
            }
        }
    }
}

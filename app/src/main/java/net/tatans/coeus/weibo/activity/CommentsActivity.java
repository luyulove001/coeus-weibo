package net.tatans.coeus.weibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.tools.TatansActivity;
import net.tatans.coeus.network.tools.TatansApplication;
import net.tatans.coeus.network.tools.TatansLog;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.view.ViewInject;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;


/**
 * 转发并评论界面
 */
public class CommentsActivity extends TatansActivity implements View.OnClickListener {
    /**
     * 分享内容
     */
    @ViewInject(id = R.id.comments_content)
    EditText comments_content;
    /**
     * 语音输入按钮
     */
    @ViewInject(id = R.id.comments_voice_input, click = "onClick")
    RelativeLayout comments_voice_input;
    /**
     * @按钮
     */
    @ViewInject(id = R.id.comments_fenxiang, click = "onClick")
    RelativeLayout comments_fenxiang;
    /**
     * 发送按钮
     */
    @ViewInject(id = R.id.comments_send, click = "onClick")
    TextView comments_send;

    //转发和评论按钮
    @ViewInject(id = R.id.forward_comment)
    LinearLayout mForwardComment;
    @ViewInject(id = R.id.checkBox)
    private CheckBox checkBox;
    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 用于获取微博信息流等操作的API
     */
    private StatusesAPI mStatusesAPI;
    //回复评论或者评论
    private CommentsAPI mCommentsAPI;
    //用于判定是做什么操作，转发，评论 , 还是发微博
    private String type;
    //评论的id
    private Long commentId;
    //微博的id
    private Long weiboId;
    //转发评论，是否转发并评论， 0不评论， 1评论给当前微博，
    private int commentType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initData();
        CheckBoxListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取当前已经保存的Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        type = getIntent().getExtras().getString(Const.TYPE);
        if (type.equals(Const.WRITE_WEIBO)) {
            //写微博
            //对statusAPI实例化
            mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
            comments_content.setHint("分享新鲜事");
            mForwardComment.setVisibility(View.GONE);
        } else if (type.equals(Const.REPLY) || type.equals(Const.WRITE_COMMENT)) {
            //回复
            mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, mAccessToken);
            weiboId = Long.parseLong(getIntent().getExtras().getString("weiboId"));
            if (type.equals(Const.REPLY)) {
                commentId = Long.parseLong(getIntent().getExtras().getString("id"));
                comments_content.setHint("回复");
            } else {
                if (!getIntent().getBooleanExtra("isReply", false))
                    comments_content.setHint("写评论");
                else comments_content.setHint("回复");
            }
            mForwardComment.setVisibility(View.GONE);
        } else {
            mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
            weiboId = Long.parseLong(getIntent().getExtras().getString("weiboId"));
        }
    }

    public void voiceClick() {
        Intent intent = new Intent();
        intent.setClass(CommentsActivity.this, VoicesActivity.class);
        startActivityForResult(intent, 1);
        View viewa = getWindow().peekDecorView();
        InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManger.hideSoftInputFromWindow(viewa.getWindowToken(), 0);

    }

    /**
     * CheckBoxListener的监听事件
     */
    private void CheckBoxListener() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    commentType = 1;//选中状态，
                } else {
                    commentType = 0;//未选中状态
                }
            }
        });
    }

    /**
     * 发送按钮
     */
    private void commentSendClick() {
        String content = comments_content.getText().toString();
        if (type.equals(Const.WRITE_WEIBO)) {
            //发送一条纯文字微博
            mStatusesAPI.update(content, null, null, mListener);
        } else if (type.equals(Const.REPLY)) {
            //回复评论
            mCommentsAPI.reply(commentId, weiboId, content, false, false, mListener);
        } else if (type.equals(Const.WEIBO_COMMENT) || type.equals(Const.WRITE_COMMENT)) {
            mCommentsAPI.create(content, weiboId, false, mListener);
        } else if (type.equals(Const.WEIBO_FORWARD)) {
            mStatusesAPI.repost(weiboId, content, commentType, mListener);
        }

    }

    private void commentsFenxiang() {
        Intent intent = new Intent(this, ContactListActivity.class);
        intent.putExtra(Const.CONTACT_OR_FOllOW, 1);
        this.startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data != null) {
                    String dataName = data.getExtras().getString(Const.CONTACT);
                    comments_content.setText(comments_content.getText().toString() + "@" + dataName + "  ");//" " 不能去掉提供@我时的一个空白字符串
                }
                break;
            case 1:
                setTitle("");
                String voiceData = data.getStringExtra("sm");
                try {
                    AccessibilityManager accessibilityManagers = (AccessibilityManager) TatansApplication
                            .getContext().getSystemService(ACCESSIBILITY_SERVICE);
                    accessibilityManagers.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (voiceData.equals("")) {
                    Toast.makeText(CommentsActivity.this, "语音识别失败" + voiceData,
                            Toast.LENGTH_LONG).show();
                } else {
                    comments_content.append(voiceData);
                    Toast.makeText(CommentsActivity.this, "" + voiceData,
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            TatansLog.d("antony", response);
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                        TatansToast.showAndCancel("获取微博信息流成功, 条数: " + statuses.statusList.size());
                    }
                } else if (response.startsWith("{\"created_at\"") && type.equals(Const.WRITE_WEIBO)) {
                    TatansToast.showAndCancel("微博发送成功");
                    finish();
                } else if (response.startsWith("{\"created_at\"") && (type.equals(Const.REPLY))) {
                    TatansToast.showAndCancel("回复一条微博成功");
                    finish();
                } else if (response.startsWith("{\"created_at\"") && type.equals(Const.WEIBO_COMMENT) || type.equals(Const.WRITE_COMMENT)) {
                    TatansToast.showAndCancel("评论一条微博成功");
                    finish();
                } else if (response.startsWith("{\"created_at\"") && type.equals(Const.WEIBO_FORWARD)) {
                    TatansToast.showAndCancel("转发成功");
                    finish();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            TatansToast.showAndCancel(info.toString());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comments_voice_input:
                voiceClick();
                break;
            case R.id.comments_fenxiang:
                commentsFenxiang();
                break;
            case R.id.comments_send:
                commentSendClick();
                break;
        }
    }
}

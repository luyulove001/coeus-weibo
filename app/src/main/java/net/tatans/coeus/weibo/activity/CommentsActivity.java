package net.tatans.coeus.weibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.network.tools.TatansApplication;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;


/**
 * 转发并评论界面
 */
@ContentView(R.layout.activity_comments)
public class CommentsActivity extends BaseActivity {
    /**
     * 分享内容
     */
    @ViewIoc(R.id.comments_content)
    EditText comments_content;
    /**
     * 语音输入按钮
     */
    @ViewIoc(R.id.comments_voice_input)
    RelativeLayout comments_voice_input;
    /**
     * @按钮
     */
    @ViewIoc(R.id.comments_fenxiang)
    RelativeLayout comments_fenxiang;
    /**
     * 发送按钮
     */
    @ViewIoc(R.id.comments_send)
    TextView comments_send;

    //转发和评论按钮
    @ViewIoc(R.id.forward_comment)
    LinearLayout mForwardComment;
    /**
     * 当前 Token 信息
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 用于获取微博信息流等操作的API
     */
    private StatusesAPI mStatusesAPI;
    //用于判定是做什么操作，转发，评论 还是发微博
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();


    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取当前已经保存的Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        //对statusAPI实例化
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);

        type = getIntent().getExtras().getString(Const.TYPE);

        if (type.equals(Const.WRITE_WEIBO)) {
            //写微博
            comments_content.setHint("分享新鲜事");
            mForwardComment.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.comments_voice_input)
    public void voiceClick() {
        Intent intent = new Intent();
        intent.setClass(CommentsActivity.this, VoicesActivity.class);
        startActivityForResult(intent, 1);
        View viewa = getWindow().peekDecorView();
        InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManger.hideSoftInputFromWindow(viewa.getWindowToken(), 0);

    }

    /**
     * 发送按钮
     */
    @OnClick(R.id.comments_send)
    private void commentSendClick() {
        String content = comments_content.getText().toString();
        if (type.equals(Const.WRITE_WEIBO)) {
            //发送一条纯文字微博
            mStatusesAPI.update(content, null, null, mListener);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            setTitle("");
            String voiceData = data.getStringExtra("sm");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(comments_content, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
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
        }
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                        TatansToast.showAndCancel("获取微博信息流成功, 条数: " + statuses.statusList.size());
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);
                    TatansToast.showAndCancel("发送一送微博成功");
                } else {
                    TatansToast.showAndCancel(response);
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            TatansToast.showAndCancel(info.toString());
        }
    };
}

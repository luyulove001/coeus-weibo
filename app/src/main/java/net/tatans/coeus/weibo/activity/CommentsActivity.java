package net.tatans.coeus.weibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.network.tools.TatansApplication;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.comments_voice_input)
    public void voiceClick() {
        Intent intent=new Intent();
        intent.setClass(CommentsActivity.this,VoicesActivity.class);
        startActivityForResult(intent, 1);View viewa = getWindow().peekDecorView();
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(viewa.getWindowToken(), 0);

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
}

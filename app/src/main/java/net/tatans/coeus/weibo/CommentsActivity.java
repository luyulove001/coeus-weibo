package net.tatans.coeus.weibo;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

/**转发并评论界面*/
@ContentView(R.layout.activity_comments)
public class CommentsActivity extends BaseActivity {
    /**分享内容*/
    @ViewIoc(R.id.comments_content)
    EditText comments_content;
    /**语音输入按钮*/
    @ViewIoc(R.id.comments_voice_input)RelativeLayout comments_voice_input;
    /**@按钮*/
    @ViewIoc(R.id.comments_fenxiang) RelativeLayout comments_fenxiang;
    /**发送按钮*/
    @ViewIoc(R.id.comments_send)
    TextView comments_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

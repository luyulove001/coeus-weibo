package net.tatans.rhea.demo;

import android.widget.TextView;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansApplication;
import net.tatans.coeus.network.tools.TatansSpeakerCallback;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

/**
 * 全局speak示例
 * Created by Administrator on 2016/4/22.
 */
@ContentView(R.layout.ttsdemo)
public class GlobalActivity extends BaseActivity {

    @ViewIoc(R.id.tts_text)
    TextView tts_text;

    //    播放
    @OnClick(R.id.tts_play)
    public void btnPlay() {
        TatansApplication.speech(tts_text.getText().toString(), 88, new TatansSpeakerCallback() {
//            开始
            @Override
            public void onSpeakBegin() {
                super.onSpeakBegin();
            }
//            暂停
            @Override
            public void onSpeakPaused() {
                super.onSpeakPaused();
            }
//            继续
            @Override
            public void onSpeakResumed() {
                super.onSpeakResumed();
            }

//          percent 播放进度
//          beginPos 开始播放
//          endPos 停止播放
            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                super.onSpeakProgress(percent, beginPos, endPos);
            }

//            播放完成
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onCompletedError() {
                super.onCompletedError();
            }
        });
    }

    //    结束
    @OnClick(R.id.tts_cancel)
    public void btnCancel() {
        TatansApplication.stop();
    }

    //    暂停
    @OnClick(R.id.tts_pause)
    public void btnPause() {
        TatansApplication.pause();
    }

    //    继续
    @OnClick(R.id.tts_resume)
    public void btnResume() {
        TatansApplication.resume();
    }

}


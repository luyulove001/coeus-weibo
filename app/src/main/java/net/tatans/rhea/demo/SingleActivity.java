package net.tatans.rhea.demo;

import android.os.Bundle;
import android.widget.TextView;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansSpeaker;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

/**
 * 单个class的speak示例
 * Created by Administrator on 2016/4/25.
 */
@ContentView(R.layout.ttsdemo)
public class SingleActivity extends BaseActivity {
    @ViewIoc(R.id.tts_text)
    TextView tts_text;
    private TatansSpeaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        单个class的speak需要的
        speaker = TatansSpeaker.create();
    }

    //    播放
    @OnClick(R.id.tts_play)
    public void btnPlay() {
        speaker.speech(tts_text.getText().toString(), 88);
    }

    //    结束
    @OnClick(R.id.tts_cancel)
    public void btnCancel() {
        speaker.stop();
    }

    //    暂停
    @OnClick(R.id.tts_pause)
    public void btnPause() {
        speaker.pause();
    }

    //    继续
    @OnClick(R.id.tts_resume)
    public void btnResume() {
        speaker.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speaker.destroy();
    }
}

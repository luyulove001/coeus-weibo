package net.tatans.coeus.weibo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.util.NetworkConnectionUtil;
import net.tatans.coeus.weibo.util.SoundUtil;
import net.tatans.coeus.weibo.view.RippleBackground;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * ClassName :VoicesActivity
 * explain :语音界面
 *
 * @author: LMB
 * Created time : 2016/5/11 14:28.
 */
public class VoicesActivity extends Activity implements OnClickListener, OnHoverListener {
    private static String TAG = VoicesActivity.class.getSimpleName();
    public static final String PREFER_NAME = "com.iflytek.setting";
    private AssetFileDescriptor fileDescriptor;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResultss = new LinkedHashMap<String, String>();
    private Intent intent = new Intent();
    private RippleBackground rippleBackground;
    private String resultData = "";
    private static int states = 0;
    private boolean flag = true;
    private boolean source = false;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voices);
        initView();
        initData();
        // 设置参数
        setParam();
    }

    private void initData() {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(WeiboApp.getInstance(),
                mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(WeiboApp.getInstance(), mInitListener);
        mSharedPreferences = getSharedPreferences(VoicesActivity.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        initSoundFile();
        mIatResultss.clear();
    }

    @Override
    protected void onStart() {
        rippleBackground.startRippleAnimation();// 啓動動畫
        startRecord();
        boolean isShowDialog = mSharedPreferences.getBoolean(
                getString(R.string.pref_key_iat_show), true);
        if (isShowDialog) {
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("听写失败,错误码：" + ret);
            } else {
            }
        } else {
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
            showTip(getString(R.string.text_begin));
        }
        super.onStart();
    }


    private void initView() {
        LinearLayout linear_voices;
        linear_voices = (LinearLayout) findViewById(R.id.linear_voices);
        linear_voices.setOnClickListener(VoicesActivity.this);
        rippleBackground = (RippleBackground) findViewById(R.id.contents);
        linear_voices.setOnHoverListener(this);
    }

    /**
     * ClassName :TaskThread2
     * explain :线程内部类，正在语音时再点击屏幕则暂停当前语音并提示，提示完后重新开始语音
     *
     * @author: LMB
     * Created time : 2016/5/11 15:09.
     */
    class TaskThread2 extends Thread {
        @Override
        public void run() {
            try {
                while (flag) {
                    Thread.currentThread().sleep(100);
                    states += 1;
                    Message msg = new Message();
                    msg.obj = states;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:

                    if (states == 40 || states > 40) {
                        flag = false;
                        source = true;
                        mIat.startListening(mRecognizerListener);
                        //开启语音动画
                        rippleBackground.startRippleAnimation();
                        startRecord();
                    }

                    break;

                case 2:

                    break;
                default:
                    break;
            }
        }
    };
    int ret = 0; // 函数调用返回值

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * Purpose:
     * explain:听写UI监听器
     *
     * @param
     * @return
     * @author: LMB
     * Created time : 2016/5/11 14:39.
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
        }

        /**
         * Purpose:
         * explain:识别回调错误.
         * @param
         * @return
         * @author: LMB
         * Created time : 2016/5/11 14:39.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }
    };
    /**
     * Purpose:
     * explain:初始化监听器。
     *
     * @param
     * @return
     * @author: LMB
     * Created time : 2016/5/11 14:39.
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * Purpose:返回避免上一界面没有返回值
     * explain:1代表返回到ReadOrReplyMessageActivity界面，2表示返回到SendMessageActivity界面
     *
     * @param
     * @return
     * @author: LMB
     * Created time : 2016/5/11 15:13.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            rippleBackground.stopRippleAnimation();// 关闭动画
            intent.setClass(VoicesActivity.this,
                    CommentsActivity.class);
            intent.putExtra("sm", resultData);
            setResult(1, intent);
            resultData = "";
            flag = false;
            VoicesActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Purpose:
     * explain:听写监听器。
     *
     * @param
     * @return
     * @author: LMB
     * Created time : 2016/5/11 14:34.
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            resultData = "";
            source = true;

        }

        /**
         * Purpose:错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
         * 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
         * explain:
         * @param
         * @return
         * @author: LMB
         * Created time : 2016/5/11 14:33.
         */
        @Override
        public void onError(SpeechError error) {
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入

            rippleBackground.stopRippleAnimation();// 关闭动画

            intent.setClass(VoicesActivity.this,
                    CommentsActivity.class);
            intent.putExtra("sm", resultData);
            setResult(1, intent);
            VoicesActivity.this.finish();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (isLast) {
            } else {
                resultData = resultData
                        + parseIatResult(results.getResultString());
            }

        }

        public String parseIatResult(String json) {
            StringBuffer ret = new StringBuffer();

            try {
                JSONTokener e = new JSONTokener(json);
                JSONObject joResult = new JSONObject(e);
                JSONArray words = joResult.getJSONArray("ws");

                for (int i = 0; i < words.length(); ++i) {
                    JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                    JSONObject obj = items.getJSONObject(0);
                    ret.append(obj.getString("w"));
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            return ret.toString();
        }

        public void onVolumeChanged(int volume, byte[] data) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            }
        }

        public void onVolumeChanged(int arg0) {
        }
    };

    /**
     * Purpose:
     * explain:语音参数设置
     *
     * @param
     * @return
     * @author: LMB
     * Created time : 2016/5/11 14:36.
     */
    public void setParam() {
        // 引擎类型
        String mEngineType = SpeechConstant.TYPE_CLOUD;
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,
                mSharedPreferences.getString("iat_vadbos_preference", "6000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,
                mSharedPreferences.getString("iat_vadeos_preference", "2000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,
                mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
        flag = false;
        resultData = "";

    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        rippleBackground.stopRippleAnimation();// 关闭动画
        super.onPause();
    }

    /**
     * Purpose:
     * explain:初始化音频文件
     *
     * @param
     * @return
     * @author: LMB
     * Created time : 2016/5/11 14:37.
     */
    private void initSoundFile() {
        AssetManager assetManager;
        assetManager = getAssets();
        try {
            fileDescriptor = assetManager.openFd("starts.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Purpose:
     * explain:无网络连接处理，开始语音时动画播放处理
     *
     * @param
     * @return
     * @author: LMB
     * Created time : 2016/5/11 14:37.
     */
    protected void startRecord() {
        if (NetworkConnectionUtil.hasNetworkConnection(getApplicationContext())) {
            SoundUtil.playMedia(fileDescriptor);
        } else {
            // 离线处理
            rippleBackground.stopRippleAnimation();// 关闭动画
            TatansToast.showAndCancel("请打开网络");
        }
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.linear_voices:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_HOVER_ENTER:
                        states = 0;
                        resultData = "";
                        mIat.stopListening();
                        rippleBackground.stopRippleAnimation();// 关闭动画
                        if (NetworkConnectionUtil.hasNetworkConnection(getApplicationContext())) {
                            TatansToast.showAndCancel(
                                    "正在录音，请在提示音后说出内容");
                        } else {
                            // 离线处理
                            rippleBackground.stopRippleAnimation();// 关闭动画
                            TatansToast.showAndCancel("请打开网络");
                        }
                        if (source) {
                            source = false;
                            flag = true;
                            new TaskThread2().start();
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return false;
    }
}

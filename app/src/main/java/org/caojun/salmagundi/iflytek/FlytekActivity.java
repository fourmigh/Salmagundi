package org.caojun.salmagundi.iflytek;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.socks.library.KLog;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * 讯飞语音
 * Created by CaoJun on 2017/7/26.
 */

@Route(path = Constant.ACTIVITY_FLYTEK)
public class FlytekActivity extends AppCompatActivity {

    private final int[] IDButtons = {R.id.btnEnglish, R.id.btnFrench, R.id.btnRussian, R.id.btnSpanish, R.id.btnHindi, R.id.btnVietnamese};
    private final int[] IDEditTexts = {R.id.etEnglish, R.id.etFrench, R.id.etRussian, R.id.etSpanish, R.id.etHindi, R.id.etVietnamese};
    private final String[] VoiceNames = {"aiscatherine", "mariane", "allabent", "gabriela", "abha", "xiaoyun"};
    private Button[] buttons;
    private EditText[] editTexts;

    private SpeechSynthesizer mTts;
    private SharedPreferences mSharedPreferences;
    // 缓冲进度
//    private int mPercentForBuffering = 0;
    // 播放进度
//    private int mPercentForPlaying = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_flytek);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
        mTts = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    showTip(getString(R.string.ft_init_error, String.valueOf(code)));
                }
            }
        });

        buttons = new Button[IDButtons.length];
        for (int i = 0;i < IDButtons.length;i ++) {
            buttons[i] = (Button) findViewById(IDButtons[i]);
            final int index = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playTTS(index);
                }
            });
        }
        editTexts = new EditText[IDEditTexts.length];
        for (int i = 0;i < IDEditTexts.length;i ++) {
            editTexts[i] = (EditText) findViewById(IDEditTexts[i]);
        }
    }

    private void setButtonEnable(boolean enable) {
        for (int i = 0;i < IDButtons.length;i ++) {
            buttons[i].setEnabled(enable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ARouter.getInstance().build(Constant.ACTIVITY_FLYTEK_TTSSETTINGS).navigation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    private void playTTS(int index) {
        if (!setParam(index)) {
            return;
        }
        setButtonEnable(false);
        String text = editTexts[index].getText().toString();
        int code = mTts.startSpeaking(text, new SynthesizerListener() {

            @Override
            public void onSpeakBegin() {
//                showTip(getString(R.string.ft_start));
            }

            @Override
            public void onSpeakPaused() {
//                showTip(getString(R.string.ft_pause));
            }

            @Override
            public void onSpeakResumed() {
//                showTip(getString(R.string.ft_resume));
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos,
                                         String info) {
                // 合成进度
//                mPercentForBuffering = percent;
//                showTip(String.format(getString(R.string.tts_toast_format),
//                        mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                // 播放进度
//                mPercentForPlaying = percent;
//                showTip(String.format(getString(R.string.tts_toast_format),
//                        mPercentForBuffering, mPercentForPlaying));
            }

            @Override
            public void onCompleted(SpeechError error) {
                if (error == null) {
//                    showTip(getString(R.string.ft_done));
                } else if (error != null) {
                    showTip(error.getPlainDescription(true));
                }
                setButtonEnable(true);
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
                // 若使用本地能力，会话id为null
                //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                //		Log.d(TAG, "session id =" + sid);
                //	}
            }
        });

        if (code != ErrorCode.SUCCESS) {
            showTip(getString(R.string.ft_tts_error, String.valueOf(code)));
        }
    }

    private boolean setParam(int index){
        if (index < 0 || index >= VoiceNames.length) {
            return false;
        }
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, VoiceNames[index]);
        //设置合成语速
        String speed = mSharedPreferences.getString("speed_preference", "50");
        KLog.d("speed", speed);
        mTts.setParameter(SpeechConstant.SPEED, speed);
        //设置合成音调
        String pitch = mSharedPreferences.getString("pitch_preference", "50");
        KLog.d("pitch", pitch);
        mTts.setParameter(SpeechConstant.PITCH, pitch);
        //设置合成音量
        String volume = mSharedPreferences.getString("volume_preference", "50");
        KLog.d("volume", volume);
        mTts.setParameter(SpeechConstant.VOLUME, volume);
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
        return true;
    }

    private void showTip(final String text) {
        Toast mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }
}

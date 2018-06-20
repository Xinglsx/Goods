package com.mingshu.goods;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mingshu.goods.utils.VoiceUtil;
import com.mingshu.goods.views.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class VoiceSearchActivity extends AppCompatActivity {
    int mAIUIState;
    String resultStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_search);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59f1906e");
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_wakeup_xiaoshan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultStr ="";
                AnaysisAgent();
            }
        });
    }

    private String getAIUIParams() {
        String params = "";
        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream ins = assetManager.open( "cfg/aiui_phone.cfg" );
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }

    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                case AIUIConstant.EVENT_WAKEUP:
                    ((ImageView)findViewById(R.id.image_robot)).setImageResource(R.drawable.image_robot_wakeup);
                    break;

                case AIUIConstant.EVENT_RESULT: {
                    //解析结果
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));

                            String sub = params.optString("sub");
                            Boolean lrst = params.optBoolean("lrst");
                            if ("nlp".equals(sub)) {
                                // 解析得到语义结果
                                resultStr = resultStr + (new JSONObject(cntJson.optString("intent"))).optString("text");
                            }
                            if(lrst){
                                startActivity(VoiceUtil.AnalyzeText(resultStr,VoiceSearchActivity.this));
                                EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } break;

                case AIUIConstant.EVENT_ERROR: {
                } break;

                case AIUIConstant.EVENT_VAD:
                    break;

                case AIUIConstant.EVENT_START_RECORD: {
                    ((ImageView)findViewById(R.id.image_robot)).setImageResource(R.drawable.image_robot_wakeup);
                } break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    ((ImageView)findViewById(R.id.image_robot)).setImageResource(R.drawable.image_robot_sleeping);

                } break;

                case AIUIConstant.EVENT_STATE:
                    break;

                case AIUIConstant.EVENT_CMD_RETURN:
                    break;
                default:
                    break;
            }
        }
    };

    private void AnaysisAgent() {
        //创建AIUIAgent
        AIUIAgent mAIUIAgent = AIUIAgent.createAgent(this, getAIUIParams(), mAIUIListener);

        // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收语音输入
        if( AIUIConstant.STATE_WORKING !=   this.mAIUIState ){
            AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
        }

        // 打开AIUI内部录音机，开始录音
        String params = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage( AIUIConstant.CMD_START_RECORD, 0, 0, params, null );
        mAIUIAgent.sendMessage(writeMsg);
    }
}

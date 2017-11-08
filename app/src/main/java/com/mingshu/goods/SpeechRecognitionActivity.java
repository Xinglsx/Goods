package com.mingshu.goods;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mingshu.goods.utils.CommonUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SpeechRecognitionActivity extends AppCompatActivity {

    TextView mNlpText;
    int mAIUIState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);
        mNlpText = (TextView)findViewById(R.id.txtVoiceResult);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59f1906e");

        findViewById(R.id.btnStartSpeak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNlpText.setText("");
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
//                    CommonUtil.DisplayToast("进入识别状态",FragmentFind.this.getActivity());
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
                            if ("nlp".equals(sub) && lrst) {
                                // 解析得到语义结果
                                String resultStr = (new JSONObject(cntJson.optString("intent"))).optString("text");
                                mNlpText.append(resultStr);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } break;

                case AIUIConstant.EVENT_ERROR: {
                    mNlpText.append( "\n" );
                    mNlpText.append( "错误: "+event.arg1+"\n"+event.info );
                } break;

                case AIUIConstant.EVENT_VAD: {
                    if (AIUIConstant.VAD_BOS == event.arg1) {
//                        CommonUtil.DisplayToast("找到vad_bos",FragmentFind.this.getActivity());
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
//                        CommonUtil.DisplayToast("找到vad_eos",FragmentFind.this.getActivity());
                    } else {
//                        CommonUtil.DisplayToast("" + event.arg2,FragmentFind.this.getActivity());
                    }
                } break;

                case AIUIConstant.EVENT_START_RECORD: {
                    CommonUtil.DisplayToast("语音搜索开始",SpeechRecognitionActivity.this);
                } break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    CommonUtil.DisplayToast("语音搜索完成",SpeechRecognitionActivity.this);

                } break;

                case AIUIConstant.EVENT_STATE: {    // 状态事件
                    mAIUIState = event.arg1;
//                    public static final int STATE_IDLE = 1;
//                    public static final int STATE_READY = 2;
//                    public static final int STATE_WORKING = 3;
//                    public static final int CMD_GET_STATE = 1;
                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
//                        CommonUtil.DisplayToast("STATE_IDLE",FragmentFind.this.getActivity());
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
//                        CommonUtil.DisplayToast("STATE_READY",FragmentFind.this.getActivity());
                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
//                        CommonUtil.DisplayToast("STATE_WORKING",FragmentFind.this.getActivity());
                    }
                } break;

                case AIUIConstant.EVENT_CMD_RETURN:{
                    if( AIUIConstant.CMD_UPLOAD_LEXICON == event.arg1 ){
//                        CommonUtil.DisplayToast( "上传"+ (0==event.arg2?"成功":"失败"),FragmentFind.this.getActivity());
                    }
                } break;

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

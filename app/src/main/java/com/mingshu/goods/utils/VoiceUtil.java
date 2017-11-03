package com.mingshu.goods.utils;

import android.content.Context;
import android.content.Intent;

import com.mingshu.goods.HelpActivity;
import com.mingshu.goods.HomeActivity;
import com.mingshu.goods.QuestionFeedbackActivity;
import com.mingshu.goods.SettingsActivity;

/**
 * Created by Lisx on 2017-10-27.
 * 语音助手
 */

public class VoiceUtil {
    //分析文字
    public static Intent AnalyzeText(String text, Context context){
        Intent intent = new Intent();
        if(text.contains("打开")){
            if(text.contains("帮助")){
                intent.setClass(context, HelpActivity.class);
            }else if(text.contains("设置")){
                intent.setClass(context, SettingsActivity.class);
            }else if(text.contains(("问题反馈")) ||text.contains("反馈问题")){
                intent.setClass(context, QuestionFeedbackActivity.class);
            }

        }else if(text.contains(("搜索"))){
            intent.setClass(context, HomeActivity.class);
            intent.putExtra("isVoice",true);
            intent.putExtra("fragmentIndex",2);
            int a = text.indexOf("搜索");
            String text1 = text.substring(text.indexOf("搜索") + 2);
            intent.putExtra("searchContext",text.substring(text.indexOf("搜索") + 2));
        }else if(text.contains("我想买")){
            intent.setClass(context, HomeActivity.class);
            intent.putExtra("isVoice",true);
            intent.putExtra("fragmentIndex",2);
            intent.putExtra("searchContext",text.substring(text.indexOf("我想买") + 3));
        }else if(text.contains("我想找")){
            intent.setClass(context, HomeActivity.class);
            intent.putExtra("isVoice",true);
            intent.putExtra("fragmentIndex",2);
            intent.putExtra("searchContext",text.substring(text.indexOf("我想找") + 3));
        }else if(text.contains("退出")){
            intent.setClass(context, HomeActivity.class);
        }else if(text.contains("分享软件")){
            intent.setClass(context, SettingsActivity.class);
        }
        else{
            intent.setClass(context, HomeActivity.class);
            intent.putExtra("FragmentIndex","2");
        }
        return intent;
    }
}

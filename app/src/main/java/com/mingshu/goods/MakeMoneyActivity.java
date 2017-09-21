package com.mingshu.goods;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mingshu.goods.databinding.ActivityMakeMoneyBinding;
import com.mingshu.goods.utils.CommonUtil;

import java.util.Timer;
import java.util.TimerTask;

import winning.framework.ScanBaseActivity;

public class MakeMoneyActivity extends ScanBaseActivity {
    private ActivityMakeMoneyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_make_money);
        binding.setTitle("移动网赚");
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeMoneyActivity.this.finish();
            }
        });
        initView();
    }

    private void initView() {
        binding.btnQutoutiaoRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String qukanPackage = "com.jifen.qukan";
//                if(CommonUtil.checkPackage(MakeMoneyActivity.this,qukanPackage)){
//                    PackageManager packageManager = getPackageManager();
//                    startActivity(packageManager.getLaunchIntentForPackage(qukanPackage));
//                }else{
                    Intent intent = new Intent();
                    intent.setClass(MakeMoneyActivity.this,WebViewActivity.class);
                    intent.putExtra("uri","http://ntuzmzq.sitongguolu.cn/1505966835113/13892/2343112101333?i=13892&1505966835113=642579013&from=singlemessage");
                    startActivity(intent);
//                }
            }
        });
        binding.txtMakeMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Tpwd", binding.txtWechat.getText().toString());
                cm.setPrimaryClip(mClipData);
                CommonUtil.DisplayToast("微信号复制成功，正在前往【微信】",MakeMoneyActivity.this);
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        skipToWechat();
                    }
                };
                timer.schedule(task,1000);
            }
        });
    }

    public void skipToWechat(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");

            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            CommonUtil.DisplayToast("检查到您手机没有安装微信，请安装后使用该功能！", this);
        }
    }

    @Override
    public void onSuccessScan(String s) {

    }
}

package com.mingshu.goods;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.DialogUtil;
import com.mingshu.goods.utils.DownLoadUtil;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import winning.framework.BaseActivity;
import winning.framework.network.NetworkEngine;

public class WelcomeActivity extends BaseActivity {

    private ApiCoreManager apiCoreManager;
    com.mingshu.goods.models.VersionInfo serviceVersionInfo;
    com.mingshu.goods.models.VersionInfo curVersion;
    Boolean isFirstIn = false;
    SharedPreferences sp;
    Intent it = new Intent();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        apiCoreManager = new ApiCoreManager(this);

        imageView = (ImageView) findViewById(R.id.image_view);
        Glide.with(this).load(R.drawable.image_welcome).dontAnimate().into(imageView);
        curVersion = CommonUtil.getAppVersionName(WelcomeActivity.this);

        ((TextView)this.findViewById(R.id.txt_version)).setText("版本号："+curVersion.version);

        if(CommonUtil.isWifiConnected(this)){
            Update();
        }else{
            skipToHome();
        }
    }

    private void skipToHome(){
        sp = WelcomeActivity.this.getSharedPreferences("first_pref", Context.MODE_PRIVATE);
        isFirstIn = sp.getBoolean(Constant.IS_FIRST_IN,true);
        if(isFirstIn){
            it.setClass(this,GuideActivity.class);
        }else{
            it.setClass(this,LoginActivity.class);
        }
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it);
                WelcomeActivity.this.finish();
            }
        };
        timer.schedule(task,2000);
    }

    private void Update(){
        //获取版本信息
        ApiCoreManager.Api api = apiCoreManager.getVersionInfo();
        api.invoke(new NetworkEngine.Success<com.mingshu.goods.models.VersionInfo>() {
            @Override
            public void callback(com.mingshu.goods.models.VersionInfo data) {
                serviceVersionInfo = data;
                if(data.versionNumber > curVersion.versionNumber){
                    //有更新，弹出提示下载
                    onDownLoad();
                } else {
                    skipToHome();
                }
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                skipToHome();
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                skipToHome();
            }
        });
    }
    private void onDownLoad() {
        AlertDialog dialog = DialogUtil.GetMyDialog(this, serviceVersionInfo.getUpdateContent(),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DownLoadUtil loadUtil = new DownLoadUtil(WelcomeActivity.this);
                String url = serviceVersionInfo.getDownloadAddress();
                loadUtil.StartDownload(url, serviceVersionInfo.getUpdateContent());
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                skipToHome();
            }
        });
        dialog.show();
    }

}

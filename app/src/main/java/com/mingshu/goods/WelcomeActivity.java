package com.mingshu.goods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.DialogUtil;
import com.mingshu.goods.utils.DownLoadUtil;
import com.mingshu.pmp.goods.R;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import winning.framework.BaseActivity;
import winning.framework.network.NetworkEngine;

public class WelcomeActivity extends BaseActivity {

    private ApiCoreManager apiCoreManager;
    com.mingshu.goods.models.VersionInfo serviceVersionInfo;
    com.mingshu.goods.models.VersionInfo curVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        apiCoreManager = new ApiCoreManager(this);

        curVersion = CommonUtil.getAppVersionName(WelcomeActivity.this);

        ((TextView)this.findViewById(R.id.txt_version)).setText("版本号："+curVersion.version);

        this.findViewById(R.id.txt_enter_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent it = new Intent(WelcomeActivity.this,LoginActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });

        if(CommonUtil.isWifiConnected(this)){
            Update();
        }else{
            skipToHome();
        }

    }

    private void skipToHome(){
        final Intent it = new Intent(this,LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it);
            }
        };
        timer.schedule(task,1000 * 3);
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

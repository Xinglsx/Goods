package com.mingshu.goods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mingshu.goods.databinding.ActivityVersionInfoBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.DialogUtil;
import com.mingshu.goods.utils.DownLoadUtil;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.network.NetworkEngine;

public class VersionInfoActivity extends ScanBaseActivity {
    ActivityVersionInfoBinding binding;
    com.mingshu.goods.models.VersionInfo curVersion;
    com.mingshu.goods.models.VersionInfo serverVersion;
    ApiCoreManager apiCoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_version_info);
        apiCoreManager = new ApiCoreManager(this);
        initData();
        initView();
    }

    private void initData() {
        binding.setTitle("版本信息");
        curVersion = CommonUtil.getAppVersionName(this);
        binding.txtCurVersion.setText(curVersion.getVersion());
        getServerVersion();
    }

    private void initView() {
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionInfoActivity.this.finish();
            }
        });
        binding.btnUpdateVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDownLoad();
            }
        });
    }

    private void onDownLoad() {
        AlertDialog dialog = DialogUtil.GetMyDialog(this, serverVersion.getUpdateContent(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DownLoadUtil loadUtil = new DownLoadUtil(VersionInfoActivity.this);
                        String url = serverVersion.getDownloadAddress();
                        loadUtil.StartDownload(url, serverVersion.getUpdateContent());
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void onSuccessScan(String s) {

    }

    private void getServerVersion(){
        //获取版本信息
        ApiCoreManager.Api api = apiCoreManager.getVersionInfo();
        api.invoke(new NetworkEngine.Success<com.mingshu.goods.models.VersionInfo>() {
            @Override
            public void callback(com.mingshu.goods.models.VersionInfo data) {
                serverVersion = data;
                binding.txtServerVersion.setText(serverVersion.getVersion());
                binding.txtUpdateContent.setText(serverVersion.getUpdateContent());
                if(serverVersion.getVersionNumber() > curVersion.getVersionNumber()){
                    binding.btnUpdateVersion.setVisibility(View.VISIBLE);
                }
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                binding.txtServerVersion.setText("服务器版本未知");
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                binding.txtServerVersion.setText("网络故障");
            }
        });
    }
}

package com.mingshu.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mingshu.goods.databinding.ActivityUserinfoEditBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;
import winning.framework.utils.ApplicationUtil;


public class UserInfoEditActivity extends ScanBaseActivity {
    private UserInfo curUser;
    private ActivityUserinfoEditBinding binding;
    private ApiCoreManager apiCoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_userinfo_edit);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        binding.setUser(curUser);
        binding.setTitle("个人信息修改");
        apiCoreManager = new ApiCoreManager(this);
        initView();
    }

    private void initView() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加等待条
                PrompUtil.startProgressDialog(UserInfoEditActivity.this,"保存中，请稍等。。。");
                saveUserInfo();
            }
        });
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoEditActivity.this.finish();
            }
        });
    }

    private void saveUserInfo() {
        ApiManager.Api api = apiCoreManager.saveUserInfo(curUser);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.DisplayToast("用户信息更新成功!",UserInfoEditActivity.this);
                ApplicationUtil.put(UserInfoEditActivity.this,Constant.USERINFO,curUser);//修改后回写
                UserInfoEditActivity.this.finish();
            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserInfoEditActivity.this);
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserInfoEditActivity.this);
            }
        });
    }


    @Override
    public void onSuccessScan(String s) {

    }
}

package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.mingshu.goods.databinding.ActivityRegisterBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.ApplicationUtil;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;

public class RegisterActivity extends ScanBaseActivity {
    private ActivityRegisterBinding binding;
    private ApiCoreManager apiCoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        ViewUtils.inject(this);
        binding.setTitle("新用户注册");
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        apiCoreManager = new ApiCoreManager(this);
    }

    public void click(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_load:
                //加等待条
                PrompUtil.startProgressDialog(this,"注册中，请稍等。。。");

                //获取用户名和密码
                String strCode = binding.txtUserCode.getText().toString();
                String password = binding.editPassword.getText().toString();
                String passwordRepeat = binding.editPasswordRepeat.getText().toString();
                if("".equals(password) ){
                    PrompUtil.stopProgessDialog();
                    CommonUtil.DisplayToast("密码未输入！",this);
                    break;
                }
                if(password.length() < 6 || password.length() > 12){
                    PrompUtil.stopProgessDialog();
                    CommonUtil.DisplayToast("请输入6到12位密码！",this);
                    break;
                }
                if(!password.equals(passwordRepeat)){
                    PrompUtil.stopProgessDialog();
                    CommonUtil.DisplayToast("您两次输入的密码不一致！",this);
                    break;
                }
                //网络访问成功后跳转
                ApiManager.Api api = apiCoreManager.registerUserInfo(strCode,password);
                api.invoke(new NetworkEngine.Success<UserInfo>() {
                    @Override
                    public void callback(UserInfo data) {
                        PrompUtil.stopProgessDialog();
                        intentHome("注册成功！",data);
                    }

                }, new NetworkEngine.Failure() {
                    @Override
                    public void callback(int code, String message, Map rawData) {
                        PrompUtil.stopProgessDialog();
                        failuerMessage(message);
                    }
                }, new NetworkEngine.Error() {

                    @Override
                    public void callback(int code, String message, Map rawData) {
                        PrompUtil.stopProgessDialog();
                        failuerMessage(message);
                    }
                });
                break;
            case R.id.txt_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                this.finish();
                break;
        }
    }

    private void intentHome(String message,UserInfo data){
        CommonUtil.DisplayToast(message,this);
        ApplicationUtil.put(this, Constant.USERINFO,data);
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void failuerMessage(String message){
        CommonUtil.ShowMsg(message,this);
    }

    @Override
    public void onSuccessScan(String s) {

    }
}

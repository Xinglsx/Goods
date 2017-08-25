package com.mingshu.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivityChangePasswordBinding;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;
import winning.framework.utils.ApplicationUtil;


public class ChangePasswordActivity extends ScanBaseActivity {
    private ApiCoreManager apiCoreManager;
    private ActivityChangePasswordBinding binding;
    private UserInfo curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_password);
        apiCoreManager = new ApiCoreManager(this);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        initView();
    }

    private void initView() {
        binding.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.this.finish();
            }
        });

        binding.btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加等待条
                PrompUtil.startProgressDialog(ChangePasswordActivity.this,"保存中，请稍等。。。");
                changePassword();
            }
        });
    }

    @Override
    public void onSuccessScan(String s) {

    }

    private void changePassword(){
        String oldPassword = binding.editOldPassword.getText().toString();
        if(oldPassword == null ||"".equals(oldPassword)){
            CommonUtil.DisplayToast("旧密码不能为空!",ChangePasswordActivity.this);
            PrompUtil.stopProgessDialog();
            return;
        }
        String newPassword = binding.editNewPassword.getText().toString();
        if(newPassword == null ||"".equals(newPassword)){
            CommonUtil.DisplayToast("新密码不能为空!",ChangePasswordActivity.this);
            PrompUtil.stopProgessDialog();
            return;
        }else if(newPassword.length() < 6){
            CommonUtil.DisplayToast("新密码不得少于六位!",ChangePasswordActivity.this);
            PrompUtil.stopProgessDialog();
            return;
        }else if(oldPassword.equals(newPassword)){
            CommonUtil.DisplayToast("新旧密码一样，无需修改!",ChangePasswordActivity.this);
            PrompUtil.stopProgessDialog();
            return;
        }

        String newPassword2 = binding.editNewPassword2.getText().toString();
        if(newPassword2 == null ||"".equals(newPassword2)){
            CommonUtil.DisplayToast("确认新密码不能为空!",ChangePasswordActivity.this);
            PrompUtil.stopProgessDialog();
            return;
        }
        if(!newPassword.equals(newPassword2)){
            CommonUtil.DisplayToast("两次输入的新密码不一致!",
                    ChangePasswordActivity.this);
            PrompUtil.stopProgessDialog();
            return;
        }
        //网络访问成功后跳转
        ApiManager.Api api = apiCoreManager.changePassword(curUser.getId(),oldPassword,newPassword);
        api.invoke(new NetworkEngine.Success<Boolean>() {
            @Override
            public void callback(Boolean data) {
                PrompUtil.stopProgessDialog();
                CommonUtil.DisplayToast("密码修改成功！",ChangePasswordActivity.this);
                ChangePasswordActivity.this.finish();
            }

        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,ChangePasswordActivity.this);
            }
        }, new NetworkEngine.Error() {

            @Override
            public void callback(int code, String message, Map rawData) {
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,ChangePasswordActivity.this);
            }
        });
    }
}

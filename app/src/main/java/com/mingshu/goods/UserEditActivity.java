package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivityUserEditBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;
import winning.framework.utils.ApplicationUtil;


public class UserEditActivity extends ScanBaseActivity {
    ApiCoreManager apiCoreManager;
    ActivityUserEditBinding binding;
    UserInfo user;
    UserInfo curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_edit);
        apiCoreManager = new ApiCoreManager(this);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        initView();
        initData();
    }

    private void initView() {
        binding.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEditActivity.this.finish();
            }
        });
        binding.btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
       user.setUsertype((short) binding.spinUserType.getSelectedItemPosition());
        ApiManager.Api api = apiCoreManager.saveUserInfo(user);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.DisplayToast("用户信息更新成功!",UserEditActivity.this);
                UserEditActivity.this.finish();
            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserEditActivity.this);
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UserEditActivity.this);
            }
        });
    }

    private void initData() {
        Intent it = this.getIntent();
        if(it.getSerializableExtra("user") != null) {
            user = (UserInfo) it.getSerializableExtra("user");
            binding.setUser(user);
        }
        List<String> data_list= new ArrayList<String>();
        data_list.add("0-普通用户");
        data_list.add("1-VIP用户");
        data_list.add("2-SVIP用户");
        data_list.add("3-特约用户");
        if(curUser.getUsertype() == 5){
            data_list.add("4-管理员");//管理员不能设置他人为管理员，只有超级管理员才可以
        }
        //适配器
        ArrayAdapter<String>  arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        binding.spinUserType.setAdapter(arr_adapter);
        binding.spinUserType.setSelection(user.getUsertype());
    }


    @Override
    public void onSuccessScan(String s) {

    }
}

package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingshu.goods.databinding.ActivitySettingsBinding;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.Constant;

import winning.framework.utils.ApplicationUtil;


public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private UserInfo curUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);
        binding.setTitle("其他设置");
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        initView();
    }

    private void initView() {
        if("guest".equals(curUser.getId())){
            binding.linlayoutChangePassword.setVisibility(View.GONE);
            binding.linlayoutChangeUserinfo.setVisibility(View.GONE);
        }

        binding.linlayoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        binding.linlayoutChangeUserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this,UserInfoEditActivity.class);
                startActivity(intent);
            }
        });
        binding.linlayoutAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }
        });
        binding.linlayoutQuestionFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this,QuestionFeedbackActivity.class);
                startActivity(intent);
            }
        });
        binding.linlayoutVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this,VersionInfoActivity.class);
                startActivity(intent);
            }
        });
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
    }
}

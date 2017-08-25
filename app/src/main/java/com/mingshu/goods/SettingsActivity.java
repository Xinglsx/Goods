package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivitySettingsBinding;


public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);
        initView();
    }

    private void initView() {
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
                intent.setClass(SettingsActivity.this,UserInfoActivity.class);
                startActivity(intent);
            }
        });
        binding.linlayoutAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.DisplayToast("功能研发中，感谢支持!",SettingsActivity.this);
            }
        });
        binding.linlayoutQuestionFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.DisplayToast("功能研发中，感谢支持!",SettingsActivity.this);
            }
        });
        binding.linlayoutVersionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.DisplayToast("功能研发中，感谢支持!",SettingsActivity.this);
            }
        });
    }
}

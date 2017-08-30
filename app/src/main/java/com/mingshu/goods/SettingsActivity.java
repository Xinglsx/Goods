package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingshu.goods.databinding.ActivitySettingsBinding;
import com.mingshu.goods.utils.CommonUtil;


public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);
        binding.setTitle("其他设置");
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
                intent.setClass(SettingsActivity.this,UserInfoEditActivity.class);
                startActivity(intent);
            }
        });
        binding.linlayoutAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("uri","http://182.61.58.192/test");
                intent.setClass(SettingsActivity.this,WebViewActivity.class);
                startActivity(intent);
//                CommonUtil.DisplayToast("功能研发中，感谢支持!",SettingsActivity.this);
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
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
    }
}

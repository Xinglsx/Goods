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
                Intent intent = new Intent(SettingsActivity.this,TextViewActivity.class);
                intent.putExtra("title","关于我们-闪荐");
                String content = "<h3>关于我们</h3>\n";
                content += "<p>我们不生产商品，也不销售商品，我们只推荐我们用着好的产品，我们只推荐性价比高的商品，我们只会给您带来便宜和便利！</p>\n";
                content += "<p>我们的目标是净化微信朋友圈。淘宝客们把我们的朋友圈冲的乱七八糟，所以我们只能把淘宝客们请到《闪荐》中来分享商品。</p>\n";
                content += "<h3>成为特约用户</h3>\n";
                content += "<h5><font color='#FF4081'>成为特约用户，即可分享自己的商品，如有需要请联系微信：ydxc608</font></h5>\n";
                content += "<h3>商务合作</h3>\n";
                content += "<h5><font color='#1849F8'>商务合作：xinglsx@126.com  微信:ydxc608</font></h5>\n";
                content += "<h3>官方主页</h3>\n";
                content += "官方主页:<a href='http://www.mingshukeji.com.cn'>http://www.mingshukeji.com.cn</a>";
                intent.putExtra("content",content);//html类型
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

package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingshu.goods.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {
    ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_help);
        binding.setTitle("常见问题帮助");
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });

    }
    public void onClick(View v) {
        String url;
        int id = v.getId();
        switch (id) {
            case R.id.linlayout_help1:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=1";
                break;
            case R.id.linlayout_help2:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=2";
                break;
            case R.id.linlayout_help3:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=3";
                break;
            case R.id.linlayout_help4:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=4";
                break;
            case R.id.linlayout_help5:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=5";
                break;
            case R.id.linlayout_help6:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=6";
                break;
            case R.id.linlayout_help7:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=7";
                break;
            case R.id.linlayout_help8:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=8";
                break;
            case R.id.linlayout_help9:
                url = "http://www.mingshukeji.com.cn/Help/Index?id=9";
                break;
            default:
                url = "http://www.mingshukeji.com.cn";
                break;
        }
        Intent intent = new Intent();
        intent.setClass(HelpActivity.this,WebViewActivity.class);
        intent.putExtra("uri",url);
        startActivity(intent);
    }
}

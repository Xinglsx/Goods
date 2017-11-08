package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingshu.goods.databinding.ActivityTestListBinding;

public class TestListActivity extends AppCompatActivity {

    private ActivityTestListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_list);
        binding.setTitle("新功能测试中心");
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestListActivity.this.finish();
            }
        });

        initView();
    }

    private void initView() {
        binding.linlayoutSpeechRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestListActivity.this,SpeechRecognitionActivity.class));
            }
        });
        binding.linlayoutSpeechSynthesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestListActivity.this,SpeechSynthesisActivity.class));
            }
        });
    }
}

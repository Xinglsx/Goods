package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.mingshu.goods.databinding.ActivityTextViewBinding;

public class TextViewActivity extends AppCompatActivity {
    private ActivityTextViewBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_text_view);
        intent = this.getIntent();
        binding.setTitle(intent.getStringExtra("title"));
        CharSequence charSequence= Html.fromHtml(intent.getStringExtra("content"));
        binding.txtMain.setText(charSequence);
        binding.txtMain.setMovementMethod(LinkMovementMethod.getInstance());
        initView();
    }

    private void initView() {
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextViewActivity.this.finish();
            }
        });
    }
}

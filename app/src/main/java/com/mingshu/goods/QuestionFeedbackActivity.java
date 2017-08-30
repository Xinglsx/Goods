package com.mingshu.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingshu.goods.databinding.ActivityQuestionFeedbackBinding;

public class QuestionFeedbackActivity extends AppCompatActivity {
    private ActivityQuestionFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_question_feedback);
        binding.setTitle("问题反馈");
        initView();
    }

    private void initView() {
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionFeedbackActivity.this.finish();
            }
        });
    }
}

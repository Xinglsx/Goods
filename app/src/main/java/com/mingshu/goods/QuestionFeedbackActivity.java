package com.mingshu.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mingshu.goods.databinding.ActivityQuestionFeedbackBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.Questions;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.ApplicationUtil;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;

public class QuestionFeedbackActivity extends ScanBaseActivity {
    private ActivityQuestionFeedbackBinding binding;
    private UserInfo curUser;
    private ApiCoreManager apiCoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_question_feedback);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        apiCoreManager = new ApiCoreManager(this);
        binding.setTitle("问题/建议反馈");
        initView();
    }

    private void initView() {
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionFeedbackActivity.this.finish();
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrompUtil.startProgressDialog(QuestionFeedbackActivity.this,"提交中，请稍等。。。");
                Questions question = new Questions();
                question.setContent(binding.getContent());
                question.setFeedbackuserid(curUser.getId());
                question.setFeedbackusernickname(curUser.getNickname());
                question.setContact(binding.getContact());
                saveQuestion(question);
            }
        });

    }

    private void saveQuestion(Questions question) {
        ApiManager.Api api = apiCoreManager.saveQuestion(question);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.DisplayToast("提交成功，感谢您的监督与支持!",QuestionFeedbackActivity.this);
                QuestionFeedbackActivity.this.finish();
            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,QuestionFeedbackActivity.this);
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,QuestionFeedbackActivity.this);
            }
        });
    }

    @Override
    public void onSuccessScan(String s) {

    }
}

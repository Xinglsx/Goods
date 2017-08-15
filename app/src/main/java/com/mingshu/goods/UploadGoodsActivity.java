package com.mingshu.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivityUploadGoodsBinding;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;

public class UploadGoodsActivity extends ScanBaseActivity {

    private ActivityUploadGoodsBinding binding;
    private ApiCoreManager apiCoreManager;
    private GoodsInfo goodsInfo;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_goods);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_goods);
        apiCoreManager = new ApiCoreManager(this);
        goodsInfo = new GoodsInfo();
        binding.setData(goodsInfo);
        initView();
    }

    private void initView() {
        binding.btnSaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodsInfo.setState((short) 0);
                PrompUtil.startProgressDialog(UploadGoodsActivity.this,"保存草稿中，请稍等。。。");
                saveGoodsInfo();
            }
        });

        binding.btnSaveSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodsInfo.setState((short) 1);
                PrompUtil.startProgressDialog(UploadGoodsActivity.this,"提交审核中，请稍等。。。");
                saveGoodsInfo();
            }
        });

    }

    private void saveGoodsInfo(){

        //推荐信息
        goodsInfo.setRecommender(userInfo.getId());
        goodsInfo.setRecommendname(userInfo.getNickname());

        ApiManager.Api api = apiCoreManager.saveGoodsInfo(goodsInfo);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg("保存成功，请等待审核",UploadGoodsActivity.this);
                UploadGoodsActivity.this.finish();
            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UploadGoodsActivity.this);
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,UploadGoodsActivity.this);
            }
        });
    }

    @Override
    public void onSuccessScan(String s) {

    }
}

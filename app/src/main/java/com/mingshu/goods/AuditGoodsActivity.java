package com.mingshu.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.mingshu.goods.databinding.ActivityAuditGoodsBinding;
import com.mingshu.goods.managers.ApiCoreManager;

import winning.framework.ScanBaseActivity;

public class AuditGoodsActivity extends ScanBaseActivity {

    private ActivityAuditGoodsBinding binding;
    private ApiCoreManager apiCoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_audit_goods);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audit_goods);
        apiCoreManager = new ApiCoreManager(this);
    }
    @Override
    public void onSuccessScan(String s) {

    }

}


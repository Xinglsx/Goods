package com.mingshu.goods;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mingshu.goods.databinding.ActivityCouponBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.CouponInfo;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.Constant;

import java.util.HashMap;
import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.network.NetworkEngine;
import winning.framework.utils.ApplicationUtil;


public class CouponActivity extends ScanBaseActivity {

    private ActivityCouponBinding binding;
    CouponInfo couponInfo;
    UserInfo curUser;
    ApiCoreManager apiCoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_coupon);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        apiCoreManager = new ApiCoreManager(this);

        Intent intent = getIntent();
        if(intent.getSerializableExtra("coupon") != null){
            couponInfo = (CouponInfo)intent.getSerializableExtra("coupon");
            binding.setCoupon(couponInfo);
        }else {
            couponInfo = new CouponInfo();
            binding.setCoupon(couponInfo);
        }
        initView();
    }
    private void initView() {
        Glide.with(this).load(couponInfo.getPict_url()).dontAnimate().into(binding.imageCoupon);
        binding.txtCouponCount.setText("("+couponInfo.getCoupon_remain_count()+"/"+
                couponInfo.getCoupon_total_count()+")");
        binding.txtCouponVolume.setText("月销:"+couponInfo.getVolume());
        binding.txtCouponZkFinalPrice.setText("￥"+couponInfo.getZk_final_price());

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatTpwd();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponActivity.this.finish();
            }
        });
    }

    private void creatTpwd(){
        Map param = new HashMap<>();
        String url = ApiCoreManager.baseURL + "/Tbk/CreateTpwd";
        param.put("text",couponInfo.getTitle());
        param.put("url",couponInfo.getCoupon_click_url());
        param.put("logo",couponInfo.getPict_url());

        getNetworkEngine().get(url, param, new NetworkEngine.Response() {
            @Override
            public void callback(String s) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Tpwd",s);
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.taobao.taobao");
                try {
                    startActivity(intent);
                } catch (Exception exp) {
                    skipToWebView(couponInfo.getCoupon_click_url());
                }
                cm.setPrimaryClip(mClipData);
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int i, String s, Map map) {
                skipToWebView(couponInfo.getCoupon_click_url());
            }
        });
    }

    private void skipToWebView(String  uri){
        Intent intent = new Intent();
        intent.setClass(CouponActivity.this,WebViewActivity.class);
        intent.putExtra("uri",uri);
        startActivity(intent);
    }

    @Override
    public void onSuccessScan(String s) {

    }



}

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
import com.mingshu.goods.databinding.ActivityGoodsBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.PrompUtil;

import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;
import winning.framework.utils.ApplicationUtil;


public class GoodsActivity extends ScanBaseActivity {

    private ActivityGoodsBinding binding;
    GoodsInfo goodsInfo;
    private int type = 0;
    private ApiCoreManager apiCoreManager;
    private UserInfo curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods);
        apiCoreManager = new ApiCoreManager(this);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);

        Intent intent = getIntent();
        if(intent.getSerializableExtra("goods") != null){
            goodsInfo = (GoodsInfo)intent.getSerializableExtra("goods");
        }
        type = intent.getIntExtra("type",0);
        switch (type){
            case 0://一般用户查看商品界面
                binding.txtRefuse.setVisibility(View.GONE);
                binding.linlayouRefuseReason.setVisibility(View.GONE);
                break;
            case 1://可上传用户查看待审核和已拒绝商品界面
                binding.txtTitle.setText("个人待审商品");
                if(goodsInfo == null || goodsInfo.getState() > 0){
                    binding.btnBuy.setVisibility(View.GONE);
                }else {
                    binding.btnBuy.setText("提交审核");
                }
                binding.txtRefuse.setVisibility(View.GONE);
                if(goodsInfo == null || goodsInfo.getState() != 3){
                    binding.linlayouRefuseReason.setVisibility(View.GONE);
                }else {
                    binding.editRefuseReason.setEnabled(false);
                }
                break;
            case 2://管理员审核界面
                //审核界面
                binding.txtTitle.setText("商品审核界面");
                binding.btnBuy.setText("审核通过");
                break;
        }
        initView();
    }
    private void initView() {
        Glide.with(this).load(goodsInfo.getImage()).dontAnimate().into(binding.imageGoods);
        binding.txtGoodDescription.setText(goodsInfo.getDescription());
        binding.txtReason.setText("推荐理由:" + goodsInfo.getReason());
        if (goodsInfo.getPrice().contains("￥")) {
            binding.txtGoodsPrice.setText("【券后】"+goodsInfo.getPrice());
        } else {
            binding.txtGoodsPrice.setText("【券后】￥" + goodsInfo.getPrice());
        }
        if (goodsInfo.getOldprice().contains("￥")) {
            binding.txtGoodsOldprice.setText("原价:"+goodsInfo.getOldprice());
        } else {
            binding.txtGoodsOldprice.setText("原价:￥" + goodsInfo.getOldprice());
        }
//        if(type == 0){
//            binding.txtClickcount.setText(String.valueOf(goodsInfo.getClickcount() + 1));
//        }else{
//            binding.txtClickcount.setText(String.valueOf(goodsInfo.getClickcount()));
//        }

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {//购买商品
                    String tempCommand = goodsInfo.getCommand();
                    if (tempCommand == null || tempCommand == "") {
                        CommonUtil.ShowMsg("此商品未维护好，无法自动跳转！", GoodsActivity.this);
                    } else {
                        if(CommonUtil.checkPackage(GoodsActivity.this,"com.taobao.taobao")) {
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData mClipData = ClipData.newPlainText("Tpwd", goodsInfo.getCommand());
                            cm.setPrimaryClip(mClipData);
                            PackageManager packageManager = getPackageManager();
                            Intent intent = packageManager.getLaunchIntentForPackage("com.taobao.taobao");
                            try {
                                startActivity(intent);
                            } catch (Exception exp) {
                                CommonUtil.ShowMsg("请先安装【手机淘宝】", GoodsActivity.this);
                            }
                        }else{
                            Intent intent = new Intent();
                            intent.setClass(GoodsActivity.this,WebViewActivity.class);
                            intent.putExtra("uri",goodsInfo.getLink());
                            startActivity(intent);
                        }
                    }
                } else if (type == 1) {//提交审核
                    goodsInfo.setState((short)1);
                    saveGoodsInfo();
                } else {//审核通过
                    goodsInfo.setState((short)2);
                    goodsInfo.setAudituser(curUser.getId());
                    goodsInfo.setAuditname(curUser.getNickname());
                    saveGoodsInfo();
                }
            }
        });

        binding.txtRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//审核不通过
                if(binding.txtReason.length() <= 0){
                    CommonUtil.ShowMsg("请写明拒绝理由！",GoodsActivity.this);
                }else{
                    goodsInfo.setState((short)3);
                    goodsInfo.setAudituser(curUser.getId());
                    goodsInfo.setAuditname(curUser.getNickname());
                    saveGoodsInfo();
                }
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsActivity.this.finish();
            }
        });
    }
    @Override
    public void onSuccessScan(String s) {

    }

    private void saveGoodsInfo(){
        ApiManager.Api api = apiCoreManager.saveGoodsInfo(goodsInfo);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg("保存成功，请等待审核",GoodsActivity.this);
                GoodsActivity.this.finish();
            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,GoodsActivity.this);
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,GoodsActivity.this);
            }
        });
    }


}

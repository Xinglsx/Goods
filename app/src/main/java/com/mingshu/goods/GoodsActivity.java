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
import com.mingshu.goods.views.WxSharePopUpWindow;
import com.mingshu.goods.wxapi.WXEntryActivity;

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
    private WxSharePopUpWindow wxSharePopUpWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods);
        apiCoreManager = new ApiCoreManager(this);
        curUser = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        wxSharePopUpWindow = new WxSharePopUpWindow(this,this);
        Intent intent = getIntent();
        if(intent.getSerializableExtra("goods") != null){
            goodsInfo = (GoodsInfo)intent.getSerializableExtra("goods");
        }
        type = intent.getIntExtra("type",0);
        switch (type){
            //可上传用户查看待审核和已拒绝商品界面
            case 1:
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
            //管理员审核界面
            case 2:
                //审核界面
                binding.txtTitle.setText("商品审核界面");
                binding.btnBuy.setText("审核通过");
                break;
            //一般用户查看商品界面
            case 0:
            default:
                binding.txtRefuse.setVisibility(View.GONE);
                binding.linlayouRefuseReason.setVisibility(View.GONE);
                binding.txtShareGoods.setVisibility(View.VISIBLE);
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

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    //领取优惠
                    String tempCommand = goodsInfo.getCommand();
                    //淘宝应用包名
                    String taobaoPackageName = "com.taobao.taobao";
                    //过期标志
                    String strOverDue = "2018-01-01";
                    if (tempCommand == null || "".equals(tempCommand)) {
                        CommonUtil.ShowMsg("此商品未维护好，无法自动跳转！", GoodsActivity.this);
                    } else if (goodsInfo.getExpirydate() != null && goodsInfo.getExpirydate().startsWith(strOverDue)){
                        //超过效期的，默认三天
                        CommonUtil.ShowMsg("对不起，您来晚了一步，优惠券已经被抢完了！", GoodsActivity.this);
                    } else {
                        if(CommonUtil.checkPackage(GoodsActivity.this,taobaoPackageName)) {
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData mClipData = ClipData.newPlainText("Tpwd", goodsInfo.getCommand());
                            cm.setPrimaryClip(mClipData);
                            PackageManager packageManager = getPackageManager();
                            Intent intent = packageManager.getLaunchIntentForPackage(taobaoPackageName);
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
                } else if (type == 1) {
                    //提交审核
                    goodsInfo.setState((short)1);
                    saveGoodsInfo();
                } else {
                    //审核通过
                    goodsInfo.setState((short)2);
                    goodsInfo.setAudituser(curUser.getId());
                    goodsInfo.setAuditname(curUser.getNickname());
                    auditGoodsInfo();
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
                    auditGoodsInfo();
                }
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsActivity.this.finish();
            }
        });

        binding.txtShareGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxSharePopUpWindow.initPopupWindow(new WxSharePopUpWindow.OnGetData() {
                    @Override
                    public void onDataCallBack(int nClick) {
                        WXEntryActivity wxEntryActivity;
                        switch (nClick){
                            case 0:
                                wxEntryActivity = new WXEntryActivity(2,goodsInfo,GoodsActivity.this,GoodsActivity.this.getIntent());
                                wxEntryActivity.shareWXSceneSession();
                                break;
                            case 1:
                                wxEntryActivity = new WXEntryActivity(2,goodsInfo,GoodsActivity.this,GoodsActivity.this.getIntent());
                                wxEntryActivity.shareWXSceneTimeline();
                                break;
                            default:
                                break;
                        }
                    }
                });
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
    private void auditGoodsInfo(){
        ApiManager.Api api = apiCoreManager.auditGoodsInfo(goodsInfo);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg("审核操作处理成功！",GoodsActivity.this);
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

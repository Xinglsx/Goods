
package com.mingshu.goods;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.mingshu.goods.databinding.ActivityRedPaperAlipayBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.views.WxSharePopUpWindow;
import com.mingshu.goods.wxapi.WXEntryActivity;

import java.util.Map;

import winning.framework.BaseActivity;
import winning.framework.network.NetworkEngine;

public class RedPaperAlipayActivity extends BaseActivity {

    private ApiCoreManager apiCoreManager;
    private ActivityRedPaperAlipayBinding binding;
    private WxSharePopUpWindow wxSharePopUpWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_red_paper_alipay);
        apiCoreManager = new ApiCoreManager(this);
        wxSharePopUpWindow = new WxSharePopUpWindow(this,this);
        binding.setTitle("支付宝红包");
        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPaperAlipayActivity.this.finish();
            }
        });
        binding.setShareClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxSharePopUpWindow.initPopupWindow(new WxSharePopUpWindow.OnGetData() {
                    @Override
                    public void onDataCallBack(int nClick) {
                        WXEntryActivity wxEntryActivity;
                        switch (nClick){
                            case 0:
                                wxEntryActivity = new WXEntryActivity(3,RedPaperAlipayActivity.this,RedPaperAlipayActivity.this.getIntent());
                                wxEntryActivity.shareWXSceneSession();
                                break;
                            case 1:
                                wxEntryActivity = new WXEntryActivity(3,RedPaperAlipayActivity.this,RedPaperAlipayActivity.this.getIntent());
                                wxEntryActivity.shareWXSceneTimeline();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });

        initView();
    }

    private void initView() {
        binding.btnPickupRedpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickupRedPaper();
            }
        });
    }

    private void PickupRedPaper(){
        //获取双十一红包口令
        ApiCoreManager.Api api = apiCoreManager.getRedPaperCommand("alipayRedPaper");
        api.invoke(new NetworkEngine.Success<String>() {
            @Override
            public void callback(String data) {
                //获取成功，可以进行跳转淘宝
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Alipay", data);
                cm.setPrimaryClip(mClipData);
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                try {
                    startActivity(intent);
                } catch (Exception exp) {
                    CommonUtil.ShowMsg("请先安装【手机支付宝】", RedPaperAlipayActivity.this);
                }
            }
        }, new NetworkEngine.Failure() {
            @Override
            public void callback(int code, String message, Map rawData) {
                //获取失败，提示失败消息
                CommonUtil.ShowMsg(message,RedPaperAlipayActivity.this);
            }
        }, new NetworkEngine.Error() {
            @Override
            public void callback(int code, String message, Map rawData) {
                //获取出错
                CommonUtil.ShowMsg("支付宝红包口令获取失败，请稍后重试!",RedPaperAlipayActivity.this);
            }
        });
    }
}

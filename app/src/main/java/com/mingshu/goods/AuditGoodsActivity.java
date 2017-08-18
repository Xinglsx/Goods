package com.mingshu.goods;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.ImageUtil;
import com.mingshu.goods.utils.ImageUtils;
import com.mingshu.goods.utils.OnCompressListener;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivityAuditGoodsBinding;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;

public class AuditGoodsActivity extends ScanBaseActivity {

    private ActivityAuditGoodsBinding binding;
    private String fileDir = "";
    private ApiCoreManager apiCoreManager;
    private InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_audit_goods);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audit_goods);
        apiCoreManager = new ApiCoreManager(this);

        binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = getMediaFileUri(1);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takeIntent, 1);
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bitmap bitmap = ImageUtil.getScaledImage(AuditGoodsActivity.this,fileDir);
                ImageUtils.with(AuditGoodsActivity.this)
                        .load(new File(fileDir))
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                CommonUtil.ShowMsg("开始！",AuditGoodsActivity.this);
                            }

                            @Override
                            public void onSuccess(String strBase64) {
                                updatePictrue(strBase64);
                            }

                            @Override
                            public void onError(Throwable e) {
                                CommonUtil.ShowMsg("转化错误！",AuditGoodsActivity.this);
                            }
                        });

            }
        });
    }

    public Uri getMediaFileUri(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Goods");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        //创建Media File
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            fileDir = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
            mediaFile = new File(fileDir);
        } else {
            return null;
        }
        return Uri.fromFile(mediaFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                //相机拍照
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.getScaledImage(AuditGoodsActivity.this,fileDir);
                    binding.imageUpload.setImageBitmap(bitmap);
                }else{
                    CommonUtil.ShowMsg("拍照已取消",AuditGoodsActivity.this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccessScan(String s) {

    }

    private void updatePictrue(String  strBase64){
        ApiManager.Api api = apiCoreManager.updatePictrue(strBase64);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg("保存成功，请等待审核",AuditGoodsActivity.this);
                AuditGoodsActivity.this.finish();
            }
        },new NetworkEngine.Failure(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,AuditGoodsActivity.this);
            }
        },new NetworkEngine.Error(){
            @Override
            public void callback(int code,String message,Map rawData){
                PrompUtil.stopProgessDialog();
                CommonUtil.ShowMsg(message,AuditGoodsActivity.this);
            }
        });
    }

}


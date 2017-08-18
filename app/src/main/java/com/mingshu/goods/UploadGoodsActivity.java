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
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.ImageUtil;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.goods.views.MyPopUpWindow;
import com.mingshu.pmp.goods.R;
import com.mingshu.pmp.goods.databinding.ActivityUploadGoodsBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import winning.framework.ScanBaseActivity;
import winning.framework.managers.ApiManager;
import winning.framework.network.NetworkEngine;
import winning.framework.utils.ApplicationUtil;

public class UploadGoodsActivity extends ScanBaseActivity{

    private ActivityUploadGoodsBinding binding;
    private ApiCoreManager apiCoreManager;
    private GoodsInfo goodsInfo;
    private UserInfo userInfo;
    private MyPopUpWindow myPopUpWindow;


    private int xiangji = 1;
    String fileDir = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_goods);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_goods);
        apiCoreManager = new ApiCoreManager(this);
        userInfo = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        myPopUpWindow = new MyPopUpWindow(this,this);
        goodsInfo = new GoodsInfo();
        binding.setData(goodsInfo);
        initView();
    }

    private void initView() {
        binding.btnAddGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myPopUpWindow.initPopupWindow();
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = getMediaFileUri(1);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takeIntent, 1);
            }
        });
        binding.btnAddBuyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myPopUpWindow.initPopupWindow();
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = getMediaFileUri(1);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takeIntent, 2);
            }
        });

        binding.btnSaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrompUtil.startProgressDialog(UploadGoodsActivity.this,"保存草稿中，请稍等。。。");
                goodsInfo.setState((short) 0);
                saveGoodsInfo();
            }
        });

        binding.btnSaveSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrompUtil.startProgressDialog(UploadGoodsActivity.this,"提交审核中，请稍等。。。");
                goodsInfo.setState((short) 1);
                saveGoodsInfo();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                //相机拍照
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.getScaledImage(UploadGoodsActivity.this,fileDir);
                    goodsInfo.setImage(ImageUtil.bitmapToBase64(bitmap));
                    binding.imageGoods.setImageBitmap(bitmap);
                }else{
                    CommonUtil.ShowMsg("拍照已取消",UploadGoodsActivity.this);
                }
                break;
            case 2:
                //相机拍照
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.getScaledImage(UploadGoodsActivity.this,fileDir);
                    goodsInfo.setBuyimage(ImageUtil.bitmapToBase64(bitmap));
                    binding.imageBuy.setImageBitmap(bitmap);
                }else{
                    CommonUtil.ShowMsg("拍照已取消",UploadGoodsActivity.this);
                }
                break;
            default:
                break;
        }
    }

}

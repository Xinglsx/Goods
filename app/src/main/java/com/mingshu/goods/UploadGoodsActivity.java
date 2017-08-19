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
    private MyPopUpWindow imagePopUpWindow;
    private MyPopUpWindow buyImagePopUpWindow;


    private int xiangji = 1;
    String fileDir = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_goods);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_goods);
        apiCoreManager = new ApiCoreManager(this);
        userInfo = (UserInfo) ApplicationUtil.get(this, Constant.USERINFO);
        goodsInfo = new GoodsInfo();
        binding.setData(goodsInfo);
        initUI();
        initView();
    }

    private void initUI() {
        imagePopUpWindow = new MyPopUpWindow(this,this);
        buyImagePopUpWindow = new MyPopUpWindow(this,this);
    }

    private void initView() {
        binding.btnAddGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePopUpWindow == null){
                    return;
                }

                imagePopUpWindow.initPopupWindow(new MyPopUpWindow.OnGetData() {
                    @Override
                    public void onDataCallBack(int nClick) {
                        switch (nClick){
                            case 0:
                                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri photoUri = getMediaFileUri(1);
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(takeIntent, 1);
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, 3);
                                break;
                            default:
                                //Toast.makeText(UploadGoodsActivity.this, "点击了取消1", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }
        });

        binding.btnAddBuyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buyImagePopUpWindow == null){
                    return;
                }

                buyImagePopUpWindow.initPopupWindow(new MyPopUpWindow.OnGetData() {
                    @Override
                    public void onDataCallBack(int nClick) {
                        switch (nClick){
                            case 0:
                                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri photoUri = getMediaFileUri(1);
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(takeIntent, 2);
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, 4);
                                break;
                            case 2:
//                                Toast.makeText(UploadGoodsActivity.this, "点击了取消2", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
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
                //image-相机拍照
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.getScaledImage(UploadGoodsActivity.this,fileDir);
                    goodsInfo.setImage(ImageUtil.bitmapToBase64(bitmap));
                    binding.imageGoods.setImageBitmap(bitmap);
                }
                break;
            case 2:
                //buyimage-相机拍照
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.getScaledImage(UploadGoodsActivity.this,fileDir);
                    goodsInfo.setBuyimage(ImageUtil.bitmapToBase64(bitmap));
                    binding.imageBuy.setImageBitmap(bitmap);
                }
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.getScaledImage(UploadGoodsActivity.this,
                            ImageUtil.selectImage(UploadGoodsActivity.this,data));
                    goodsInfo.setImage(ImageUtil.bitmapToBase64(bitmap));
                    binding.imageGoods.setImageBitmap(bitmap);
                }
                //image从相册中选择
                break;
            case 4:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = ImageUtil.getScaledImage(UploadGoodsActivity.this,
                            ImageUtil.selectImage(UploadGoodsActivity.this,data));
                    goodsInfo.setBuyimage(ImageUtil.bitmapToBase64(bitmap));
                    binding.imageBuy.setImageBitmap(bitmap);
                }
                //buyimage从相册中选择
                break;
            default:
                break;
        }
    }



}

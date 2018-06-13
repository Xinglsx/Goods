package com.mingshu.goods;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.mingshu.goods.databinding.ActivityUploadGoodsBinding;
import com.mingshu.goods.managers.ApiCoreManager;
import com.mingshu.goods.models.GoodsInfo;
import com.mingshu.goods.models.UserInfo;
import com.mingshu.goods.utils.CommonUtil;
import com.mingshu.goods.utils.Constant;
import com.mingshu.goods.utils.ImageUtil;
import com.mingshu.goods.utils.PrompUtil;
import com.mingshu.goods.views.MyPopUpWindow;

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

    String fileDir = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        binding.setTitle("上传商品信息");
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
                            default:
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
                if(CommonUtil.isWifiConnected(UploadGoodsActivity.this)){
                    PrompUtil.startProgressDialog(UploadGoodsActivity.this,"保存草稿中，请稍等。。。");
                    saveGoodsInfo((short)0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadGoodsActivity.this);
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setTitle("温馨提示");
                    builder.setMessage("非WI-FI环境，确定要继续吗？");
                    builder.setCancelable(true);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PrompUtil.startProgressDialog(UploadGoodsActivity.this,"保存草稿中，请稍等。。。");
                            saveGoodsInfo((short)0);
                        }
                    });
                    builder.create().show();
                }
            }
        });

        binding.btnSaveSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonUtil.isWifiConnected(UploadGoodsActivity.this)){
                    PrompUtil.startProgressDialog(UploadGoodsActivity.this,"提交审核中，请稍等。。。");
                    saveGoodsInfo((short)1);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadGoodsActivity.this);
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setTitle("温馨提示");
                    builder.setMessage("非WI-FI环境，确定要继续吗？");
                    builder.setCancelable(true);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PrompUtil.startProgressDialog(UploadGoodsActivity.this,"保存草稿中，请稍等。。。");
                            saveGoodsInfo((short)1);
                        }
                    });
                    builder.create().show();
                }
            }
        });

        binding.btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paseGoodsInfo();
            }
        });

        binding.setBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadGoodsActivity.this.finish();
            }
        });
    }

    public Boolean checkData(){

        if(binding.txtGoodsDescription.length() <= 0){
            CommonUtil.DisplayToast("请输入商品描述！",this);
            return false;
        }
        if(binding.txtGoodsLink.length() <= 0){
            CommonUtil.DisplayToast("请输入商品链接！",this);
            return false;
        }
        if(binding.txtGoodsCommand.length() <= 0){
            CommonUtil.DisplayToast("请输入商品吱口令！",this);
            return false;
        }
        if(binding.txtGoodsPrice.length() <= 0){
            CommonUtil.DisplayToast("请输入商品价格！",this);
            return false;
        }
        if(binding.txtGoodsReason.length() <= 0){
            CommonUtil.DisplayToast("请输入推荐理由！",this);
            return false;
        }
        //管理员上传，无需50字理由
        if(userInfo.getUsertype() < 4 && binding.txtGoodsReason.length() < 50){
            CommonUtil.DisplayToast("请补充推荐理由，至少50字！",this);
            return false;
        }
        return true;
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

    private void saveGoodsInfo(short state){

        if(!checkData()){
            PrompUtil.stopProgessDialog();
            return ;
        }
        //管理员上传，无需审核
        if(userInfo.getUsertype() >= 4 && state == 1){
            goodsInfo.setState((short)2);
            goodsInfo.setAudituser(userInfo.getId());
            goodsInfo.setAuditname(userInfo.getNickname());
        }else{
            goodsInfo.setState(state);
        }
        //推荐信息
        goodsInfo.setRecommender(userInfo.getId());
        goodsInfo.setRecommendname(userInfo.getNickname());

        ApiManager.Api api = apiCoreManager.saveGoodsInfo(goodsInfo);
        api.invoke(new NetworkEngine.Success<Boolean>(){
            @Override
            public void callback(Boolean result){
                PrompUtil.stopProgessDialog();
                CommonUtil.DisplayToast("保存成功，请等待审核",UploadGoodsActivity.this);
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

    private  void paseGoodsInfo()
    {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = cm.getPrimaryClip();
        String content = mClipData.getItemAt(mClipData.getItemCount() - 1).getText().toString();
        //圣手三阶四阶魔方二三五阶顺滑成人比赛盲拧益智玩具套装学生初学【包邮】
        //【在售价】10.10元
        //【券后价】5.10元
        //【下单链接】http://e22a.com/h.KnZPt7
        //    -----------------
        //            复制这条信息，￥2kFy02xYXBx￥ ，打开【手机淘宝】即可查看
        if (content.length() > 0){
            int startIndex = 0;
            int endIndex = content.indexOf("【在售价】") - 1;
            if(endIndex >0 && endIndex > startIndex){
                binding.txtGoodsDescription.setText(content.substring(startIndex,endIndex));
            }

            startIndex = content.indexOf("【在售价】") + 5;
            endIndex = content.indexOf("【券后价】") - 2;
            if(startIndex  >= 0 && endIndex > startIndex) {
                binding.txtGoodsOldprice.setText("￥" + content.substring(startIndex, endIndex));
            }

            startIndex = content.indexOf("【券后价】") + 5;
            endIndex = content.indexOf("【下单链接】") - 2;
            if(startIndex  >= 0 && endIndex > startIndex) {
                binding.txtGoodsPrice.setText("￥" + content.substring(startIndex, endIndex));
            }

            startIndex = content.indexOf("【下单链接】") + 6;
            endIndex = content.indexOf("-----") - 1;
            if(startIndex  >= 0 && endIndex >0 && endIndex > startIndex) {
                binding.txtGoodsLink.setText(content.substring(startIndex, endIndex));
            }

            startIndex = content.indexOf("復·制这段描述") + 8;
            endIndex = content.indexOf("咑閞【手机淘宝】") - 2;
            if(startIndex  >= 0 && endIndex >0 && endIndex > startIndex) {
                binding.txtGoodsCommand.setText(content.substring(startIndex, endIndex));
            }
        }else{
            CommonUtil.DisplayToast("粘贴板内无内容！",this);
        }
    }
}

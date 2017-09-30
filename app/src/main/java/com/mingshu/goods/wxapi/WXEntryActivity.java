package com.mingshu.goods.wxapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingshu.goods.R;
import com.mingshu.goods.utils.CommonUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

import java.io.ByteArrayOutputStream;

import static com.mingshu.goods.wxapi.WXEntryActivity.SHARE_TYPE.Type_WXSceneSession;
import static com.mingshu.goods.wxapi.WXEntryActivity.SHARE_TYPE.Type_WXSceneTimeline;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
//    分享或收藏的目标场景，通过修改scene场景值实现。
//    发送到聊天界面——WXSceneSession
//    发送到朋友圈——WXSceneTimeline
//    添加到微信收藏——WXSceneFavorite

    private String APP_ID = "wx0c8d58cd3ecdd18b";

    private IWXAPI iwxapi;

    enum SHARE_TYPE {Type_WXSceneSession, Type_WXSceneTimeline}

    public void shareWXSceneSession(View view) {
        share(Type_WXSceneSession,2);
    }

    public void shareWXSceneTimeline(View view) {
        share(Type_WXSceneTimeline,2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        iwxapi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        iwxapi.handleIntent(getIntent(), this);
        iwxapi.registerApp(APP_ID);
    }

    private void share(SHARE_TYPE type,int contextType) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        switch (contextType){
            case 1://文字类
                WXTextObject textObject = new WXTextObject();
                textObject.text = "这个是来测试分享的！";

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObject;
                msg.description = textObject.text;

                req.transaction = buildTransaction("Req");
                req.message = msg;
                break;
            case 2://图片类
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image_icon);
                WXImageObject imgObj = new WXImageObject(bmp);

                WXMediaMessage msg2 = new WXMediaMessage();
                msg2.mediaObject = imgObj;
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,16,16,true);
                bmp.recycle();
                msg2.thumbData = Util.bmpToByteArray(thumbBmp,true);

                req.transaction = buildTransaction("img");
                req.message = msg2;
                break;
            case 3://网页类
                WXWebpageObject webPage = new WXWebpageObject();
                webPage.webpageUrl = "http://www.mingshukeji.com.cn/Coupon";

                WXMediaMessage msg3 = new WXMediaMessage(webPage);
                msg3.title = "嗨，欢迎来到MSKJ！";
                msg3.description = "这是一个导购类网站，天猫淘宝大额优惠券随便领取！ ";
                Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.image_make_money);
                msg3.thumbData = Util.bmpToByteArray(bmp1,true);

                req.transaction = buildTransaction("webpage");
                req.message = msg3;
                iwxapi.sendReq(req);
                break;
            case 4:
                break;
            default:
                break;
        }
        switch (type) {
            case Type_WXSceneSession:
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case Type_WXSceneTimeline:
                req.scene =  SendMessageToWX.Req.WXSceneTimeline;
                break;
        }
        iwxapi.sendReq(req);

        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        String result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        CommonUtil.DisplayToast(result,this);
        finish();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

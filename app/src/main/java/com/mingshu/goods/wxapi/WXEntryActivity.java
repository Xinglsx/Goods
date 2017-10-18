package com.mingshu.goods.wxapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

public class WXEntryActivity implements IWXAPIEventHandler {
//    分享或收藏的目标场景，通过修改scene场景值实现。
//    发送到聊天界面——WXSceneSession
//    发送到朋友圈——WXSceneTimeline
//    添加到微信收藏——WXSceneFavorite

    private String APP_ID = "wx0c8d58cd3ecdd18b";
    private IWXAPI iwxapi;
    private int contextType;//1-分享软件
    enum SHARE_TYPE {Type_WXSceneSession, Type_WXSceneTimeline}
    private Context context;
    private Intent intent;

    public void shareWXSceneSession() {
        share(Type_WXSceneSession);
    }

    public void shareWXSceneTimeline() {
        share(Type_WXSceneTimeline);
    }
    public WXEntryActivity(){}
    public WXEntryActivity(int contextType,Context context,Intent intent){
        this.contextType = contextType;
        this.context = context;
        this.intent = intent;

        iwxapi = WXAPIFactory.createWXAPI(this.context, APP_ID, false);
        iwxapi.handleIntent(this.intent, this);
        iwxapi.registerApp(APP_ID);
    }

    private void share(SHARE_TYPE type) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        switch (contextType){
            case 1:
                //分享软件-进入闪荐下载界面
                WXWebpageObject webPage = new WXWebpageObject();
                webPage.webpageUrl = "http://www.mingshukeji.com.cn/Home/SjDownload";

                WXMediaMessage msg3 = new WXMediaMessage(webPage);
                msg3.title = "闪荐，闪电推荐更便宜的好东西";
                msg3.description = "内有大量天猫淘宝【大额】、【限时】、【限量】优惠券，无需注册随便领取！ ";
                Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_sj_icon_128);
                msg3.thumbData = Util.bmpToByteArray(bmp1,true);

                req.transaction = buildTransaction("webpage");
                req.message = msg3;
                break;
            case 2:
                //图片类
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_icon);
                WXImageObject imgObj = new WXImageObject(bmp);

                WXMediaMessage msg2 = new WXMediaMessage();
                msg2.mediaObject = imgObj;
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,16,16,true);
                bmp.recycle();
                msg2.thumbData = Util.bmpToByteArray(thumbBmp,true);

                req.transaction = buildTransaction("img");
                req.message = msg2;
                break;
            case 3:
                //文字类
                WXTextObject textObject = new WXTextObject();
                textObject.text = "这个是来测试分享的！";

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObject;
                msg.description = textObject.text;

                req.transaction = buildTransaction("Req");
                req.message = msg;
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
        CommonUtil.DisplayToast(result,context);
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

package com.mingshu.goods.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Lisx on 2017-06-28.
 * 等待框工具类
 */

public class PrompUtil {
    private static ProgressDialog dialog = null;

    //开始等待框
    public static void startProgressDialog(Context context,String message){
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        dialog.setMessage(message);// 设置ProgressDialog 提示信息
        dialog.setCancelable(true); // 设置ProgressDialog 是否可以按退回按键取消
        dialog.setIndeterminate(false); // 设置ProgressDialog 的进度条是否不明确
        dialog.show();
    }

    //结束等待框
    public static void stopProgessDialog(){
        dialog.hide();
    }
}

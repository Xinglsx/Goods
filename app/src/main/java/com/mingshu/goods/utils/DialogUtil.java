package com.mingshu.goods.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Lisx on 2017-06-28.
 */

public class DialogUtil {
    /**
     * 创建一个dialog
     *
     * @param context
     * @param message  dialog提示框内容
     * @param listener 确定按钮的监听事件
     * @return
     */
    public static AlertDialog GetMyDialog(Context context, String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener1) {
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(message).setTitle("更新提示").setPositiveButton("确认", listener).setNegativeButton("取消", listener1).create();
        dialog.setCancelable(false); // 是否可以按返回键消失
        dialog.setCanceledOnTouchOutside(false); //点击加载框以外的区域
        return dialog;
    }


    /**
     * 创建一个dialog
     *
     * @param context
     * @param message  dialog提示框内容
     * @param listener 确定按钮的监听事件
     * @return
     */
    public static AlertDialog GetMyDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(message).setTitle("提示").setPositiveButton("确认", listener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.setCancelable(false); // 是否可以按返回键消失
        dialog.setCanceledOnTouchOutside(false); //点击加载框以外的区域
        return dialog;
    }
}

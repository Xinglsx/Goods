package com.mingshu.goods.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    /**
     * 弹出提示框
     * @param message 弹出消息
     * @param context 上下文
     */
    public static void ShowMsg(String message,Context context) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("提示");
        dlg.setMessage(message);
        dlg.setPositiveButton("确定",null);
        dlg.show();
    }

    /**
     * 闪现提示
     * @param message 弹出消息
     * @param context 上下文
     */
    public static void DisplayToast(String message,Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 时间戳转日期
     * @param str 时间戳
     * @param format 需要转换的格式
     */
    public static String stampToDate(String str,String format) {
        if(str == null || str == "")
            return "";
        if (str.contains("Date(") && str.contains("+")) {
            str = str.substring(str.indexOf("(") + 1, str.indexOf("+"));
        }
        String result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(str);
        Date date = new Date(lt);
        result = simpleDateFormat.format(date);
        return result;
    }

    /**
     * 返回当前程序版本名
     */
    public static com.mingshu.goods.models.VersionInfo getAppVersionName(Context context) {
        com.mingshu.goods.models.VersionInfo versionInfo = new com.mingshu.goods.models.VersionInfo();
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionInfo.setVersionNumber(pi.versionCode);
            versionInfo.setVersion(pi.versionName);

        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionInfo;
    }

    /**
     * 判断Wifi网络是否可用
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接信息
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}

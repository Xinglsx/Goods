package com.mingshu.goods.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
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
        if(str == null || "".equals(str))
            return "";
        if (str.contains("Date(") && str.contains("+")) {
            str = str.substring(str.indexOf("(") + 1, str.indexOf("+"));
        }
        //2017-08-27T09:47:58.623 mscorlib.dll v4.5.2下的显示格式
        String result;
        if(str.contains("T")){
            str = str.replace("T"," ");
            if(str.length() > 19){
                switch (format){
                    case Constant.DATEFORMAT_DATE:
                        return str.substring(0,10);
                    case Constant.DATEFORMAT_DATETIME_MM:
                        return str.substring(0,16);
                    case Constant.DATEFORMAT_DATETIME_SS:
                        return str.substring(0,19);
                    case Constant.DATEFORMAT_DATE_WITHOUTLINE:
                        return str.substring(0,10).replace("-","");
                    case Constant.DATEFORMAT_DATETIME_WITHOUTLINE:
                        return str.substring(0,10).replace("-","").
                                replace(" ","").replace(":","");
                    default:
                        return str;
                }
            }
            else{
                return str;
            }
        }
        else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            long lt = new Long(str);
            Date date = new Date(lt);
            result = simpleDateFormat.format(date);
        }
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

    /**
     * 获取软件的缓存的目录
     * @param mContext
     * @return
     */
    public static String getDiskCacheDir(Context mContext) {
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if(mContext.getExternalCacheDir() == null){
                cachePath = "";
            }else{
                cachePath = mContext.getExternalCacheDir().getPath();
            }
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return cachePath;
    }

    /*
    *
    * */
    public static boolean checkPackage(Context context,String packageName)
    {
        if (packageName == null || "".equals(packageName))
            return false;
        try{
            context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }catch (PackageManager.NameNotFoundException e){
            return false;
        }

    }
}

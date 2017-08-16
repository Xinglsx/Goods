package com.mingshu.goods.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
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
     * 闪现提示
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
     * 获取设备唯一标示
     *
     * @param context
     * @return
     */
//    public static String getDeviceId(Context context) {
//        StringBuilder deviceId = new StringBuilder();
//        // 渠道标志
//        deviceId.append("android--");
//        try {
//
//            //IMEI（imei）
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String imei = tm.getDeviceId();
//            if (!TextUtils.isEmpty(imei)) {
//                deviceId.append("imei");
//                deviceId.append(imei);
//                return deviceId.toString();
//            }
//            //wifi mac地址
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = wifi.getConnectionInfo();
//            String wifiMac = info.getMacAddress();
//            if (!TextUtils.isEmpty(wifiMac)) {
//                deviceId.append("wifi");
//                deviceId.append(wifiMac);
//                return deviceId.toString();
//            }
//            //序列号（sn）
//            String sn = tm.getSimSerialNumber();
//            if (!TextUtils.isEmpty(sn)) {
//                deviceId.append("sn");
//                deviceId.append(sn);
//                return deviceId.toString();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return deviceId.toString();
//    }
}

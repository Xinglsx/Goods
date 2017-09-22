package com.mingshu.goods.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.android.volley.VolleyLog.TAG;


/**
 * Created by Lisx on 2017-08-16.
 */

public class ImageUtil {
    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 将图片转成16位的字符串
     *
     * @return time
     */

    public static String getImage_String(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);// (0 - 100)压缩文件
        byte[] bt = stream.toByteArray();
        return byte2hex(bt);
    }

    /**
     * 将16位的字符串转成图片
     */
    public static Bitmap string2Image(String str){
        byte[] bytes = hex2byte(str);//字符串转二进制
        //二进度转图片
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }

    /**
     * 二进制转字符串(图片压缩使用)
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }
    /**
     * 字符串转二进制
     * @param str 字符串
     */
    public static byte[] hex2byte(String str) {
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 图片质量压缩
     *
     * @return bm
     */

    public static Bitmap getScaledImage(Activity context, String path) {

        //1.获取图片宽高
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //不解析图片的像素，只解析图片的宽高
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int imageWidth = opts.outWidth;
        int imageHeight = opts.outHeight;

        //2.获取屏幕宽高
        Display dp = context.getWindowManager().getDefaultDisplay();
        int screenWidth = dp.getWidth();
        int screenHeight = dp.getHeight();
        //3.计算缩小比例
        int scale = 2;
        int scaleWidth = imageWidth / screenWidth;
        int scaleHeight = imageHeight / screenHeight;

        if (scaleWidth >= scaleHeight && scaleWidth > 1) {
            scale = scaleWidth;
        } else if (scaleWidth < scaleHeight && scaleHeight > 1) {
            scale = scaleHeight;
        }
        //4.先缩小，再加载
        //指定缩小比例
        opts.inSampleSize = scale;
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, opts);
    }

    public static String selectImage(Context context,Intent data){
        Uri selectedImage = data.getData();
//      Log.e(TAG, selectedImage.toString());
        if(selectedImage!=null){
            String uriStr=selectedImage.toString();
            String path=uriStr.substring(10,uriStr.length());
            if(path.startsWith("com.sec.android.gallery3d")){
                Log.e(TAG, "It's auto backup pic path:"+selectedImage.toString());
                return null;
            }
        }
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        if(selectedImage != null) {
            Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        }else{
            return null;
        }

    }
}

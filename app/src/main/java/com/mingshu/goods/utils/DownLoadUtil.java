package com.mingshu.goods.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mingshu.goods.managers.AppManager;
import com.mingshu.goods.views.MyHrpProgressDialog;

import java.io.File;


/**
 * 下载
 * Created by xpf on 2016/9/19.
 */
public class DownLoadUtil {
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + "/Hrp.apk";
    private Activity activity;
    private MyHrpProgressDialog progressDialog;
    private File file;

    public DownLoadUtil(Activity activity) {
        this.activity = activity;
    }

    public void StartDownload(String url, String message) {
        if (url == null || "".equals(url)) {
            CommonUtil.DisplayToast( "暂无下载链接",activity);
            return;
        }
        file = new File(BASE_PATH);
        if (file.exists()) {
            file.delete();
        }
        progressDialog = new MyHrpProgressDialog(activity);
        progressDialog.setTitle("更新提示");
        if (message == null || "".equals(message))
            progressDialog.setMessage("正在下载...");
        else
            progressDialog.setMessage(message);
        progressDialog.show();
        HttpUtils http = new HttpUtils();
        /**
         第一个参数：网络下载位置
         第二个参数：下载保存位置
         第三个参数：如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
         第四个参数：如果从请求返回信息中获取到文件名，下载完成后自动重命名。
         第五个参数：下载的监听
         */
        http.download(url, BASE_PATH, true, false, new RequestCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                showUpdataDialog();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                CommonUtil.DisplayToast("下载失败，稍后再试",activity);
                progressDialog.dismiss();
            }
        });
    }

    private void showUpdataDialog() {
        AlertDialog dialog = DialogUtil.GetMyDialog(activity, "是否安装新版本", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (file != null) {
                    InstallApk(file);
                }
            }
        });
        dialog.show();
    }

    private void InstallApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
        AppManager.getAppManager().finishAllActivity();
    }
}

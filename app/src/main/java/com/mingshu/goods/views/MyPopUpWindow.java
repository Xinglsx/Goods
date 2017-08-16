package com.mingshu.goods.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mingshu.pmp.goods.R;

import java.io.File;

/**
 * Description: MyPopUpWindow
 * Author: Jiang
 * Date:   2017/2/3
 */
public class MyPopUpWindow{
    private PopupWindow popupWindow;
    private int from = 0;
    private Context context;
    private Activity activity;

    private int xiangji = 1;
    private File sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");

    public MyPopUpWindow(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * 菜单弹出方向
     */
    public enum Location {

        LEFT,
        RIGHT,
        TOP,
        BOTTOM;

    }

    public void initPopupWindow() {
        from = Location.BOTTOM.ordinal();
        View popupWindowView = activity.getLayoutInflater().inflate(R.layout.layout_picture_selector, null);
        //内容，高度，宽度
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        } else {
            popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, true);
        }
        //菜单背景色
//        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //宽度
        //popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        //高度
        //popupWindow.setHeight(LayoutParams.FILL_PARENT);
        //显示位置
        if (Location.LEFT.ordinal() == from) {
            popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.activity_upload_goods, null), Gravity.LEFT, 0, 500);
        } else if (Location.RIGHT.ordinal() == from) {
            popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.activity_upload_goods, null), Gravity.RIGHT, 0, 500);
        } else if (Location.BOTTOM.ordinal() == from) {
            popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.activity_upload_goods, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(1.0f);
        //关闭事件
        popupWindow.setOnDismissListener(new PopupDismissListener());

        Button open = (Button) popupWindowView.findViewById(R.id.picture_selector_take_photo_btn);
        Button save = (Button) popupWindowView.findViewById(R.id.picture_selector_pick_picture_btn);
        Button close = (Button) popupWindowView.findViewById(R.id.picture_selector_cancel_btn);


        open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "拍照", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "从相册选择", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "取消", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
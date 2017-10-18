package com.mingshu.goods.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mingshu.goods.R;

/**
 * Description: MyPopUpWindow
 * Author: Jiang
 * Date:   2017/2/3
 */
public class MyPopUpWindow{
    private PopupWindow popupWindow;
    int from = 0;
    private Context context;
    private Activity activity;

    // 数据接口
    OnGetData mOnGetData;

    public MyPopUpWindow(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public MyPopUpWindow(){}

    /**
     * 菜单弹出方向
     */
    public enum Location {

        LEFT,
        RIGHT,
        TOP,
        BOTTOM,

    }

    public void initPopupWindow(OnGetData sd) {

        mOnGetData = sd;

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

        popupWindowView.findViewById(R.id.picture_selector_take_photo_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnGetData.onDataCallBack(0);//拍照
                popupWindow.dismiss();
            }
        });

        popupWindowView.findViewById(R.id.picture_selector_pick_picture_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnGetData.onDataCallBack(1);//从相册选择
                popupWindow.dismiss();
            }
        });

        popupWindowView.findViewById(R.id.picture_selector_cancel_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnGetData.onDataCallBack(2);//取消
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

    // 数据接口抽象方法
    public interface OnGetData {
        abstract void onDataCallBack(int nClick);
    }
}


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
public class WxSharePopUpWindow{
    private PopupWindow popupWindow;
    int from = 0;
    private Context context;
    private Activity activity;

    /**
     * 数据接口
     */
    OnGetData mOnGetData;
    public WxSharePopUpWindow(){}
    public WxSharePopUpWindow(Context context, Activity activity) {
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
        BOTTOM,

    }

    public void initPopupWindow(OnGetData sd) {

        mOnGetData = sd;

        from = Location.BOTTOM.ordinal();
        View popupWindowView = activity.getLayoutInflater().inflate(R.layout.layout_share_selector, null);
        //内容，高度，宽度
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        } else {
            popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, true);
        }
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //显示位置
        if (Location.LEFT.ordinal() == from) {
            popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.activity_home, null), Gravity.LEFT, 0, 500);
        } else if (Location.RIGHT.ordinal() == from) {
            popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.activity_home, null), Gravity.RIGHT, 0, 500);
        } else if (Location.BOTTOM.ordinal() == from) {
            popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.activity_home, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(1.0f);
        //关闭事件
        popupWindow.setOnDismissListener(new PopupDismissListener());

        popupWindowView.findViewById(R.id.share_selector_friend).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //分享给朋友
                mOnGetData.onDataCallBack(0);
                popupWindow.dismiss();
            }
        });

        popupWindowView.findViewById(R.id.share_selector_friend_circle).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //分享到朋友圈
                mOnGetData.onDataCallBack(1);
                popupWindow.dismiss();
            }
        });

        popupWindowView.findViewById(R.id.share_selector_cancel_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //取消
                mOnGetData.onDataCallBack(2);
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
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 数据接口抽象方法
     */
    public interface OnGetData {
        abstract void onDataCallBack(int nClick);
    }
}

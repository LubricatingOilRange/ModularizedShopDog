package com.library.core.helper;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.library.core.base.BaseApplication;
import com.library.core.module.window.popup.CommonPopupWindow;

public class PopupWindowHelper {
    private static final int GRAVITY = Gravity.CENTER;

    /*
     * 默认显示中间
     *
     * @param parent
     * @param context
     * @param commonPopupWindow
     */
    public static CommonPopupWindow showNormal(final Context context, View parent, String[] arrayData, CommonPopupWindow commonPopupWindow) {
        if (commonPopupWindow == null) {
            commonPopupWindow = new CommonPopupWindow.Builder(context)
                    .setBackGroundLevel(0.5f)//window展示的时候，背景透明度
                    .setOutsideTouchable(true)//设置点击窗口外部是否消失
                    .setWidthAndHeight(BaseApplication.SCREEN_WIDTH * 2 / 3, BaseApplication.SCREEN_HEIGHT / 3)//设置PopupWindow的宽高
//                  .setAnimationStyle(R.style.anim_dialog)//设置动画的样式ID
                    .setView(0, null)
                    .create(new CommonPopupWindow.OnPopupWindowDismiss() {
                        @Override
                        public void onDismiss() {
                            //CustomToast.create(context).shortShow("popupWindow is dismissed");
                        }
                    });
        }

        //数据的填充
        View contentView = commonPopupWindow.getContentView();
        TextView tv_window = contentView.findViewById(0);
        TextView bt_window = contentView.findViewById(0);

        tv_window.setText(arrayData[0]);
        bt_window.setText(arrayData[1]);

        commonPopupWindow.showNormal(parent, GRAVITY);//设置显示在parent的左边
        return commonPopupWindow;
    }
}

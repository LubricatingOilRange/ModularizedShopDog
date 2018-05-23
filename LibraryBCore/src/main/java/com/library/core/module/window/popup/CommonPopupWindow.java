package com.library.core.module.window.popup;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.library.core.helper.ViewHolderHelper;

public class CommonPopupWindow extends PopupWindow {

    public interface OnPopupWindowDismiss {
        void onDismiss();
    }

    private OnPopupWindowDismiss mOnPopupWindowDismiss;


    final PopupController mPopupController;

    CommonPopupWindow(Context context, OnPopupWindowDismiss onPopupWindowDismiss) {
        super(context);
        this.mOnPopupWindowDismiss = onPopupWindowDismiss;
        mPopupController = new PopupController(context, this);
    }

    /*
     * 弹框的高度
     * @return
     */
    @Override
    public int getHeight() {
        return super.getHeight();
    }

    /*
     * 弹框的宽度
     * @return
     */
    @Override
    public int getWidth() {
        return super.getWidth();
    }

    /**
     * 弹框小时
     */
    @Override
    public void dismiss() {
        super.dismiss();
        mPopupController.setBackGroundLevel(1.0f);// 恢复透明度
        if (mOnPopupWindowDismiss != null) {
            mOnPopupWindowDismiss.onDismiss();
        }
    }

    //--------------------------------popupWindow的显示位置---------------------------------------

    /*
     * 默认显示parent的位置Gravity.CENTER
     * @param parent
     * @param gravity
     * @return
     */
    public PopupWindow showNormal(View parent, int gravity) {
        this.showAtLocation(parent, gravity, 0, 0);
        return this;
    }

    /*
     * 默认显示parent的位置Gravity.CENTER
     * @param parent
     * @param gravity
     * @return
     */
    public PopupWindow showLeft(View parent, int gravity) {
        this.showAtLocation(parent, gravity, 0, 0);
        return this;
    }

    /*
     * 默认显示parent的位置Gravity.CENTER
     * @param parent
     * @param gravity
     * @return
     */
    public PopupWindow showUp(View parent, int gravity) {
        this.showAtLocation(parent, gravity, 0, 0);
        return this;
    }

    /*
     * 默认显示parent的位置Gravity.CENTER
     * @param parent
     * @param gravity
     * @return
     */
    public PopupWindow showRight(View parent, int gravity) {
        this.showAtLocation(parent, gravity, 0, 0);
        return this;
    }

    /*
     * 展示在目标parent的左下方
     * @param parent
     * @param gravity
     * @return
     */
    public PopupWindow showBottom(View parent, int gravity) {
//        int[] viewLocation = ViewUtil.getViewLocation(parent);
//        int[] viewWH = ViewUtil.getViewWH(parent);
        this.showAtLocation(parent, gravity, 0, 0);
        return this;
    }

    //仿AlertDialog中的Builder
    public static class Builder {
        private final PopupController.PopupParams param;


        public Builder(Context context) {
            this.param = new PopupController.PopupParams(context);
        }

        /*
         * 设置PopupWindow的布局ID
         * @param layoutResId
         * @return
         */
        public Builder setView(int layoutResId, OnHandleViewCallBack onHandleViewCallBack) {
            param.mView = null;//重复创建的时候蒋之前的View清楚
            param.layoutResId = layoutResId;
            param.mOnHandleViewCallBack = onHandleViewCallBack;
            return this;
        }

        /*
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view, OnHandleViewCallBack onHandleViewCallBack) {
            param.mView = view;
            param.layoutResId = 0;
            param.mOnHandleViewCallBack = onHandleViewCallBack;
            return this;
        }

        /*
         * 设置宽度和高度 如果不设置 默认是wrap_content
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            param.mWidth = width;
            param.mHeight = height;
            return this;
        }

        /*
         * 设置背景灰色程度
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            param.isShowBg = true;
            param.bg_level = level;
            return this;
        }

        /*
         * 是否可点击Outside消失
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            param.isTouchable = touchable;
            return this;
        }

        /*
         * 设置动画
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            param.isShowAnim = true;
            param.animationStyle = animationStyle;
            return this;
        }

        /*
         * popupWindow的创建
         * @return
         */
        public CommonPopupWindow create(OnPopupWindowDismiss onPopupWindowDismiss) {
            final CommonPopupWindow popupWindow = new CommonPopupWindow(param.mContext, onPopupWindowDismiss);
            param.apply(popupWindow.mPopupController);
            return popupWindow;
        }
    }

    /**
     * 处理布局的回掉接口
     */
    public interface OnHandleViewCallBack {
        void onHandleView(CommonPopupWindow commonPopupWindow, ViewHolderHelper holder);
    }
}

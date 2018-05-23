package com.library.core.module.window.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.library.core.R;
import com.library.core.util.ViewTool;

public class CommonDialog extends Dialog {

    final DialogController mDialogController;

    public CommonDialog(Context context) {
        this(context, 0);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        mDialogController = new DialogController(context, this);
    }

    //将Dialog的属性传递给DialogController.DialogParam进行管理
    public static class Builder {
        private DialogController.DialogParam mDialogParam;

        public Builder(Context context) {
            mDialogParam = new DialogController.DialogParam(context);
        }

        /*
         * 设置Dialog的布局ID
         * @mDialogParam layoutResId
         * @return
         */
        public Builder setView(int layoutResId, CommonDialogFragment.OnHandleViewCallBack callBack) {
            mDialogParam.mView = null;//重复创建的时候蒋之前的View清楚
            mDialogParam.layoutResId = layoutResId;
            mDialogParam.mHandleViewCallBack = callBack;
            return this;
        }

        public Builder setView(int layoutResId) {
            return setView(layoutResId, null);
        }

        /*
         * view 设置Dialog布局
         * @param view
         * @param callBack  处理布局回掉
         * @return
         */
        public Builder setView(View view, CommonDialogFragment.OnHandleViewCallBack callBack) {
            mDialogParam.mView = view;
            mDialogParam.layoutResId = 0;
            mDialogParam.mHandleViewCallBack = callBack;
            return this;
        }

        public Builder setView(View view) {
            return setView(view, null);
        }

        /*
         * 设置dialog的window的宽高
         *
         * @param width  宽
         * @param height 高
         */
        public Builder setWidthAndHeight(int width, int height) {
            mDialogParam.mWidth = width;
            mDialogParam.mHeight = height;
            return this;
        }

        /*
         * 设置dialog的window的坐标点
         *
         * @param x  X轴
         * @param y Y轴
         */
        public Builder setXAndY(int x, int y) {
            mDialogParam.xValue = x;
            mDialogParam.yValue = y;
            return this;
        }

        /*
         * @mDialogParam gravity 设置Dialog弹框位置
         * @return Builder
         * Gravity.Left,right,center,top,bottom
         */
        public Builder setGravity(int gravity) {
            mDialogParam.mGravity = gravity;
            return this;
        }

        /*
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         */
        public Builder setBackGroundLevel(float level) {
            mDialogParam.isShowBg = true;
            mDialogParam.bg_level = level;
            return this;
        }

        /*
         * 设置Outside是否可点击
         *
         * @param touchable 是否可点击
         */
        public Builder setOutsideTouchable(boolean touchable) {
            mDialogParam.isTouchable = touchable;//设置outside可点击
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            mDialogParam.isShowAnim = true;
            mDialogParam.animationStyle = animationStyle;
            return this;
        }

        /*
         * CommonDialog的创建
         * @param themeId （主题样式）
         * @return
         */
        public CommonDialog create(int themeId) {
            final CommonDialog commonDialog = new CommonDialog(mDialogParam.mContext, themeId);
            mDialogParam.apply(commonDialog.mDialogController);
            ViewTool.measureWidthAndHeight(commonDialog.mDialogController.mDialogView);
            return commonDialog;
        }

        /*
         *  带默认主题样式的
         * @return
         */
        public CommonDialog create() {
            return create(R.style.library_core_dialog);
        }
    }
}

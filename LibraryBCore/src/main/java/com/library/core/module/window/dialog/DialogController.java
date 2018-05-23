package com.library.core.module.window.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.library.core.helper.ViewHolderHelper;


class DialogController {
    private int mLayoutResId;//布局id
    private View mView;//用来区分setView的参数是layoutID 还是View

    private Context mContext;
    private CommonDialog mCommonDialog;
    private int width, height;//弹窗的宽和高
    private int xValue, yValue;//弹窗坐标（左上方）
    private float bg_level;//屏幕背景灰色程度
    private int animationStyle;//动画
    private int gravity = Gravity.CENTER;//设置默认中心显示

    View mDialogView;

    DialogController(Context context, CommonDialog commonDialog) {
        this.mContext = context;
        this.mCommonDialog = commonDialog;
    }

    private void setView(@LayoutRes int layoutResId, CommonDialogFragment.OnHandleViewCallBack handleViewCallBack) {
        this.mLayoutResId = layoutResId;
        this.mView = null;
        installContent(handleViewCallBack);
    }

    private void setView(View view, CommonDialogFragment.OnHandleViewCallBack handleViewCallBack) {
        this.mView = view;
        this.mLayoutResId = 0;
        installContent(handleViewCallBack);
    }

    private void installContent(CommonDialogFragment.OnHandleViewCallBack handleViewCallBack) {
        if (mLayoutResId != 0) {
            mDialogView = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
        } else if (mView != null) {
            mDialogView = mView;
        }
        mCommonDialog.setContentView(mDialogView);

        //回调处理DialogView
        if (handleViewCallBack != null) {
            handleViewCallBack.onHandleView(new ViewHolderHelper(mDialogView));
        }
    }

    /**
     * 设置dialog的window的宽高
     *
     * @param width  宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 设置dialog的window的坐标点
     *
     * @param x X轴
     * @param y Y轴
     */
    private void setXAndY(int x, int y) {
        this.xValue = x;
        this.yValue = y;
    }

    /**
     * 设置背景灰色程度
     *
     * @param level 0.0f-1.0f
     */
    private void setBackGroundLevel(float level) {
        this.bg_level = level;
    }

    /**
     * @param gravity 显示位置
     */
    private void setGravity(int gravity) {
        this.gravity = gravity;
    }

    /**
     * 设置动画
     */
    private void setAnimationStyle(int animationStyle) {
        this.animationStyle = animationStyle;
    }

    /**
     * 设置Outside是否可点击
     *
     * @param touchable 是否可点击
     */
    private void setOutsideTouchable(boolean touchable) {
        mCommonDialog.setCanceledOnTouchOutside(touchable);//设置outside可点击
        mCommonDialog.setCancelable(touchable);
    }

    /**
     * 根据bg_level，
     */
    void setDialogAttr() {
        Window window = mCommonDialog.getWindow();
        if (window == null) {
            return;
        }
        if (this.animationStyle != 0) {
            window.setWindowAnimations(animationStyle);
        }
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.alpha = this.bg_level;
        //设置宽高
        if (this.width != 0 && this.height != 0) {
            attr.width = this.width;
            attr.height = this.height;
        }
        attr.gravity = this.gravity;
        //设置坐标,给予展示的点的偏移量
        if (xValue != 0 || yValue != 0) {
            attr.x = xValue;
            attr.y = yValue;
        }

//        dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗。
//        如果要达到背景全部变暗的效果，需要设置  dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        ，否则，背景无效果
//        attributes.dimAmount = bg_level;//设置弹框的背景
    }


    static class DialogParam {
        Context mContext;
        int animationStyle;//外部传进的动画样式ID
        View mView;//外部传递过来的View
        int layoutResId;//外部传递过来的ViewId,需要转换成View
        int mWidth, mHeight;//弹窗的宽和高
        int xValue, yValue;//弹窗的坐标点
        boolean isShowBg, isShowAnim;
        float bg_level;//屏幕背景灰色程度
        boolean isTouchable = true;
        int mGravity;
        CommonDialogFragment.OnHandleViewCallBack mHandleViewCallBack;

        DialogParam(Context mContext) {
            this.mContext = mContext;
        }

        void apply(DialogController controller) {
            if (mView != null) {
                controller.setView(mView, mHandleViewCallBack);
            } else if (layoutResId != 0) {
                controller.setView(layoutResId, mHandleViewCallBack);
            } else {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }
            controller.setWidthAndHeight(mWidth, mHeight);
            controller.setXAndY(xValue, yValue);
            controller.setOutsideTouchable(isTouchable);//设置outside可点击
            controller.setGravity(mGravity);
            if (isShowBg) {
                //设置背景
                controller.setBackGroundLevel(bg_level);
            }
            if (isShowAnim) {
                //设置动画
                controller.setAnimationStyle(animationStyle);
            }

            controller.setDialogAttr();//设置弹框的属性
        }
    }
}

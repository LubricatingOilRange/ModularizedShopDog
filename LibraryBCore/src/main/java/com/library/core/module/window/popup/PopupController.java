package com.library.core.module.window.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.library.core.helper.ViewHolderHelper;

// popupWindow 的管理类
public class PopupController {
    private int layoutResId;//布局id
    private View mView;//用来区分setView的参数是layoutID 还是View

    private Context context;
    private CommonPopupWindow mPopupWindow;

    private View mPopupView;//弹窗布局View

    PopupController(Context context, CommonPopupWindow commonPopupWindow) {
        this.context = context;
        this.mPopupWindow = commonPopupWindow;
    }

    public void setView(int layoutResId, CommonPopupWindow.OnHandleViewCallBack onHandleViewCallBack) {
        mView = null;
        this.layoutResId = layoutResId;
        installContent(onHandleViewCallBack);
    }

    public void setView(View view, CommonPopupWindow.OnHandleViewCallBack onHandleViewCallBack) {
        mView = view;
        this.layoutResId = 0;
        installContent(onHandleViewCallBack);
    }

    private void installContent(CommonPopupWindow.OnHandleViewCallBack onHandleViewCallBack) {
        if (layoutResId != 0) {
            mPopupView = LayoutInflater.from(context).inflate(layoutResId, null);
        } else if (mView != null) {
            mPopupView = mView;
        }
        mPopupWindow.setContentView(mPopupView);

        //回调处理DialogView
        if (onHandleViewCallBack != null) {
            onHandleViewCallBack.onHandleView(mPopupWindow, new ViewHolderHelper(mPopupView));
        }
    }

    /**
     * 设置宽度
     *
     * @param width  宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没设置宽高，默认是WRAP_CONTENT
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            mPopupWindow.setWidth(width);
            mPopupWindow.setHeight(height);
        }
    }

    /**
     * 设置背景灰色程度
     *
     * @param level 0.0f-1.0f
     */
    void setBackGroundLevel(float level) {
        Window mWindow = ((Activity) context).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }

    /**
     * 设置动画
     */
    private void setAnimationStyle(int animationStyle) {
        mPopupWindow.setAnimationStyle(animationStyle);
    }

    /**
     * 设置Outside是否可点击
     *
     * @param touchable 是否可点击
     */
    private void setOutsideTouchable(boolean touchable) {
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置透明背景
        mPopupWindow.setOutsideTouchable(touchable);//设置outside可点击
        mPopupWindow.setFocusable(touchable);
    }

    //弹框的参数
    static class PopupParams {
        Context mContext;
        int layoutResId;//布局id
        int mWidth, mHeight;//弹窗的宽和高
        int animationStyle;//动画Id
        boolean isShowBg, isShowAnim;
        float bg_level;//屏幕背景灰色程度
        View mView;
        boolean isTouchable = true;//窗口外部是否可点击或点击消失

        CommonPopupWindow.OnHandleViewCallBack mOnHandleViewCallBack;

        PopupParams(Context mContext) {
            this.mContext = mContext;
        }

        void apply(PopupController controller) {
            if (mView != null) {
                controller.setView(mView, mOnHandleViewCallBack);
            } else if (layoutResId != 0) {
                controller.setView(layoutResId, mOnHandleViewCallBack);
            } else {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }
            controller.setWidthAndHeight(mWidth, mHeight);
            controller.setOutsideTouchable(isTouchable);//设置outside可点击
            if (isShowBg) {
                //设置背景
                controller.setBackGroundLevel(bg_level);
            }
            if (isShowAnim) {
                controller.setAnimationStyle(animationStyle);
            }
        }
    }
}

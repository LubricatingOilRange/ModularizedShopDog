package com.library.core.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewTool {

    private ViewTool() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //是否可见
    public static void setVisible(View view, boolean visible) {
        if (view == null) {
            return;
        }
        if (visible) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
    }

    //设置不可见
    public static void setINVisible(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    //是否可用
    public static void setEnable(View view, boolean enable) {
        if (view == null) {
            return;
        }
        if (enable) {
            if (!view.isEnabled()) {
                view.setEnabled(true);
            }
        } else {
            if (view.isEnabled()) {
                view.setEnabled(false);
            }
        }
    }

    //是否select
    public static void setSelect(View view, boolean isSelect) {
        if (view == null) {
            return;
        }
        if (isSelect) {
            if (!view.isSelected()) {
                view.setSelected(true);
            }
        } else {
            if (view.isSelected()) {
                view.setSelected(false);
            }
        }

    }

    /*
     * 根据父View 更新文本
     *
     * @param parentView
     * @param layoutId (TextView,Button)
     */
    public static void setTextById(View parentView, int childViewId, String content) {
        if (parentView == null) {
            return;
        }
        TextView textView = parentView.findViewById(childViewId);
        if (textView != null) {
            textView.setText(content);
        }
    }

    /**
     * 获取view的宽度
     */
    public static int getViewWidth(View view) {
        measureView(view);
        return view.getMeasuredWidth();
    }

    /**
     * 获取view的高度
     */
    public static int getMeasureViewHeight(View view) {
        measureView(view);
        return view.getMeasuredHeight();
    }

    /**
     * 测量view
     */
    private static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
    }

    public static void measureWidthAndHeight(View view) {
        //设置测量模式为UNSPECIFIED可以确保测量不受父View的影响
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        //得到测量宽度
        int mWidth = view.getMeasuredWidth();
        //得到测量高度
        int mHeight = view.getMeasuredHeight();
//        LogUtil.i("a", "mWidth:" + mWidth + "/mHeight:" + mHeight);
    }
}

package com.library.core.module.auto.utils;

import android.util.TypedValue;
import android.view.View;

import com.library.core.R;
import com.library.core.module.auto.AutoLayoutInfo;
import com.library.core.module.auto.attr.Attrs;
import com.library.core.module.auto.attr.AutoAttr;
import com.library.core.module.auto.config.AutoLayoutConfig;

public class AutoUtil {

    private static int getComplexUnit(int data) {
        return TypedValue.COMPLEX_UNIT_MASK & (data);
    }

    public static boolean isPxVal(TypedValue val) {
        return val != null && val.type == TypedValue.TYPE_DIMENSION &&
                getComplexUnit(val.data) == TypedValue.COMPLEX_UNIT_PX;
    }

    /**
     * 会直接将view的LayoutParams上设置的width，height直接进行百分比处理
     *
     * @param view
     */
    public static void auto(View view) {
        autoSize(view);
        autoPadding(view);
        autoMargin(view);
        autoTextSize(view, AutoAttr.BASE_DEFAULT);
    }

    /**
     * @param view
     * @param attrs #Attrs.WIDTH|Attrs.HEIGHT
     * @param base  AutoAttr.BASE_WIDTH|AutoAttr.BASE_HEIGHT|AutoAttr.BASE_DEFAULT
     */
    public static void auto(View view, int attrs, int base) {
        AutoLayoutInfo autoLayoutInfo = AutoLayoutInfo.getAttrFromView(view, attrs, base);
        if (autoLayoutInfo != null)
            autoLayoutInfo.fillAttrs(view);
    }

    public static void autoTextSize(View view) {
        auto(view, Attrs.TEXTSIZE, AutoAttr.BASE_DEFAULT);
    }

    public static void autoTextSize(View view, int base) {
        auto(view, Attrs.TEXTSIZE, base);
    }

    public static void autoMargin(View view) {
        auto(view, Attrs.MARGIN, AutoAttr.BASE_DEFAULT);
    }

    public static void autoMargin(View view, int base) {
        auto(view, Attrs.MARGIN, base);
    }

    public static void autoPadding(View view) {
        auto(view, Attrs.PADDING, AutoAttr.BASE_DEFAULT);
    }

    public static void autoPadding(View view, int base) {
        auto(view, Attrs.PADDING, base);
    }

    public static void autoSize(View view) {
        auto(view, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_DEFAULT);
    }

    public static void autoSize(View view, int base) {
        auto(view, Attrs.WIDTH | Attrs.HEIGHT, base);
    }

    public static boolean autoed(View view) {
        Object tag = view.getTag(R.id.library_tag_auto_size);
        if (tag != null) return true;
        view.setTag(R.id.library_tag_auto_size, "Just Identify");
        return false;
    }

    public static float getPercentWidth1px() {
        int screenWidth = AutoLayoutConfig.getInstance().getScreenWidth();
        int designWidth = AutoLayoutConfig.getInstance().getDesignWidth();
        return 1.0f * screenWidth / designWidth;
    }

    public static float getPercentHeight1px() {
        int screenHeight = AutoLayoutConfig.getInstance().getScreenHeight();
        int designHeight = AutoLayoutConfig.getInstance().getDesignHeight();
        return 1.0f * screenHeight / designHeight;
    }

    public static int getPercentWidthSize(int val) {
        int screenWidth = AutoLayoutConfig.getInstance().getScreenWidth();
        int designWidth = AutoLayoutConfig.getInstance().getDesignWidth();
        return (int) (val * 1.0f / designWidth * screenWidth);
    }

    public static int getPercentWidthSizeBigger(int val) {
        int screenWidth = AutoLayoutConfig.getInstance().getScreenWidth();
        int designWidth = AutoLayoutConfig.getInstance().getDesignWidth();

        int res = val * screenWidth;
        if (res % designWidth == 0) {
            return res / designWidth;
        } else {
            return res / designWidth + 1;
        }

    }

    public static int getPercentHeightSizeBigger(int val) {
        int screenHeight = AutoLayoutConfig.getInstance().getScreenHeight();
        int designHeight = AutoLayoutConfig.getInstance().getDesignHeight();

        int res = val * screenHeight;
        if (res % designHeight == 0) {
            return res / designHeight;
        } else {
            return res / designHeight + 1;
        }
    }

    public static int getPercentHeightSize(int val) {
        int screenHeight = AutoLayoutConfig.getInstance().getScreenHeight();
        int designHeight = AutoLayoutConfig.getInstance().getDesignHeight();

        return (int) (val * 1.0f / designHeight * screenHeight);
    }
}

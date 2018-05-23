package com.library.core.module.auto.attr;

import android.view.View;

import com.library.core.util.LogUtil;
import com.library.core.module.auto.utils.AutoUtil;

public abstract class AutoAttr {
    public static final int BASE_WIDTH = 1;
    public static final int BASE_HEIGHT = 2;
    public static final int BASE_DEFAULT = 3;

    protected int pxVal;
    protected int baseWidth;
    protected int baseHeight;

    /*
    protected boolean isBaseWidth;
    protected boolean isBaseDefault;

    public AutoAttr(int pxVal)
    {
        this.pxVal = pxVal;
        isBaseDefault = true;
    }

    public AutoAttr(int pxVal, boolean isBaseWidth)
    {
        this.pxVal = pxVal;
        this.isBaseWidth = isBaseWidth;
    }
 */

    public AutoAttr(int pxVal, int baseWidth, int baseHeight) {
        this.pxVal = pxVal;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }

    public void apply(View view) {

        boolean log = view.getTag() != null && view.getTag().toString().equals("auto");

        if (log) {
            LogUtil.e(" pxVal = " + pxVal + " ," + this.getClass().getSimpleName());
        }
        int val;
        if (useDefault()) {
            val = defaultBaseWidth() ? getPercentWidthSize() : getPercentHeightSize();
            if (log) {
                LogUtil.e(" useDefault val= " + val);
            }
        } else if (baseWidth()) {
            val = getPercentWidthSize();
            if (log) {
                LogUtil.e(" baseWidth val= " + val);
            }
        } else {
            val = getPercentHeightSize();
            if (log) {
                LogUtil.e(" baseHeight val= " + val);
            }
        }

        if (val > 0)
            val = Math.max(val, 1);//for very thin divider
        execute(view, val);
    }

    protected int getPercentWidthSize() {
        return AutoUtil.getPercentWidthSizeBigger(pxVal);
    }

    protected int getPercentHeightSize() {
        return AutoUtil.getPercentHeightSizeBigger(pxVal);
    }


    protected boolean baseWidth() {
        return contains(baseWidth, attrVal());
    }

    protected boolean useDefault() {
        return !contains(baseHeight, attrVal()) && !contains(baseWidth, attrVal());
    }

    protected boolean contains(int baseVal, int flag) {
        return (baseVal & flag) != 0;
    }

    protected abstract int attrVal();

    protected abstract boolean defaultBaseWidth();

    protected abstract void execute(View view, int val);


    @Override
    public String toString() {
        return "AutoAttr{" +
                "pxVal=" + pxVal +
                ", baseWidth=" + baseWidth() +
                ", defaultBaseWidth=" + defaultBaseWidth() +
                '}';
    }
}

package com.library.core.module.auto.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.library.core.R;
import com.library.core.module.auto.utils.AutoUtil;

public class AutoTabLayout extends TabLayout {
    private static final int NO_VALID = -1;
    private int mTextSize;
    private boolean mTextSizeBaseWidth = false;

    public AutoTabLayout(Context context) {
        this(context, null);
    }

    public AutoTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint({"CustomViewStyleable","PrivateResource"})
    public AutoTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initTextSizeBaseWidth(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, R.style.Widget_Design_TabLayout);
        int tabTextAppearance = a.getResourceId(R.styleable.TabLayout_tabTextAppearance,
                R.style.TextAppearance_Design_Tab);

        mTextSize = loadTextSizeFromTextAppearance(tabTextAppearance);
        a.recycle();
    }

    @SuppressLint("CustomViewStyleable")
    private void initTextSizeBaseWidth(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Library_Auto_TabLayout);
        mTextSizeBaseWidth = a.getBoolean(R.styleable.Library_Auto_TabLayout_whether_text_size, false);
        a.recycle();
    }

    private int loadTextSizeFromTextAppearance(int textAppearanceResId) {
        TypedArray a = getContext().obtainStyledAttributes(textAppearanceResId,
                R.styleable.TextAppearance);

        try {
            if (!AutoUtil.isPxVal(a.peekValue(R.styleable.TextAppearance_android_textSize)))
                return NO_VALID;
            return a.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, NO_VALID);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void addTab(Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
        setUpTabTextSize(tab);
    }

    @Override
    public void addTab(Tab tab, boolean setSelected) {
        super.addTab(tab, setSelected);
        setUpTabTextSize(tab);
    }

    private void setUpTabTextSize(Tab tab) {
        if (mTextSize == NO_VALID || tab.getCustomView() != null) return;

        ViewGroup tabGroup = (ViewGroup) getChildAt(0);
        ViewGroup tabContainer = (ViewGroup) tabGroup.getChildAt(tab.getPosition());
        TextView textView = (TextView) tabContainer.getChildAt(1);


        if (AutoUtil.autoed(textView)) {
            return;
        }
        int autoTextSize = 0;
        if (mTextSizeBaseWidth) {
            autoTextSize = AutoUtil.getPercentWidthSize(mTextSize);
        } else {
            autoTextSize = AutoUtil.getPercentHeightSize(mTextSize);
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, autoTextSize);
    }
}

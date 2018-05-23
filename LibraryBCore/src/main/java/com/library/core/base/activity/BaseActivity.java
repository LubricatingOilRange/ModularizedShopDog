package com.library.core.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.library.core.R;
import com.library.core.util.StatusBarUtil;
import com.library.core.module.auto.AutoLayoutActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//无MVP的Activity基类
public abstract class BaseActivity extends AutoLayoutActivity {

    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mBind = ButterKnife.bind(this);
        if (isStatusBar()) {//设置状态栏的颜色
            StatusBarUtil.setColor(this, ContextCompat.getColor(this,getStatusBarColor() ));
        }
        onCreateInit();//在onCreate方法初始化页面
    }

    protected abstract int getLayoutId();

    protected abstract void onCreateInit();

    //是否设置状态栏
    protected boolean isStatusBar() {
        return true;
    }
    //获取状态栏的颜色
    protected int getStatusBarColor() {
        return R.color.color_0079BF;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }
}

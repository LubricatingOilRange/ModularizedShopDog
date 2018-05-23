package com.library.core.base.mvp;

//  Created by ruibing.han on 2018/3/29.

import com.library.core.base.activity.BaseInputActivity;

//MVP 页面 -- 当页面有EditText时 实现点击EditText外面后先进行隐藏软键盘效果
public abstract class BaseMvpInputActivity<T extends BasePresenter> extends BaseInputActivity implements BaseView {

    protected T mPresenter;

    @Override
    public void onCreateInit() {
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        onCreateInitPageAndData();//页面进行初始化操作
    }

    protected abstract T getPresenter();//获取Presenter对象

    protected abstract void onCreateInitPageAndData();//初始化页面和数据

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroy();
    }
}

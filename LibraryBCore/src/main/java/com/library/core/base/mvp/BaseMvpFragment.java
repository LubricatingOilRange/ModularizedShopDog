package com.library.core.base.mvp;

import android.app.Activity;

import com.library.core.base.fragment.BaseFragment;

public abstract class BaseMvpFragment<T extends BasePresenter, A extends Activity> extends BaseFragment<A> implements BaseView {

    protected T mPresenter;

    @Override
    protected void onViewCreatedInit() {
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        onViewCreatedInitPageAndData();//初始化页面和数据
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroyView();
    }

    protected abstract T getPresenter();//获取Presenter对象

    protected abstract void onViewCreatedInitPageAndData();//进行页面和数据初始化

}

package com.library.core.module.http.rxjava;

import android.util.Log;
import android.widget.Toast;

import com.library.core.base.mvp.BaseView;
import com.library.core.module.http.exception.AppException;
import com.library.core.module.http.exception.ExceptionHelper;
import com.library.core.util.LogUtil;

import io.reactivex.subscribers.ResourceSubscriber;

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private BaseView mView;
    private boolean isShowErrorState;

    public CommonSubscriber(BaseView view) {
        this(view, true);
    }

    public CommonSubscriber(BaseView view, boolean isShowErrorState) {
        this.mView = view;
        this.isShowErrorState = isShowErrorState;
    }

    @Override
    public void onNext(T t) {
        LogUtil.d("onNext");
    }

    @Override
    public void onComplete() {
        LogUtil.d("onComplete");
    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if (isShowErrorState) {
            if (e instanceof AppException) {
                mView.showErrorMsg((AppException) e);
            } else {
                mView.showErrorMsg(new AppException(ExceptionHelper.DEFAULT_ERROR_CODE, "获取失败"));
            }
        }
    }

}

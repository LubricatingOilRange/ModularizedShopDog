package com.library.core.base.mvp;

//  Created by ruibing.han on 2018/5/15.

public interface BasePresenter<T> {

    void attachView(T view);

    void detachView();
}

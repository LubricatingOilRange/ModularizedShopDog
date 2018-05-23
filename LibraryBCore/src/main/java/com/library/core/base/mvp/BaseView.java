package com.library.core.base.mvp;

//  Created by ruibing.han on 2018/5/15.

import com.library.core.module.http.exception.AppException;

public interface BaseView {

    void showErrorMsg(AppException e);//展示错误信息
}

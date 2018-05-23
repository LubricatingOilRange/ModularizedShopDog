package com.library.common.app;

//  Created by ruibing.han on 2018/5/15.

import android.os.Environment;

import java.io.File;

public class Constant {
    public static final boolean DEBUG = true;//当前是否是在测试阶段

    public static final String DEFAULT_ERROR_CODE = "-1";//默认的错误码

    public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "ShopDog";
}

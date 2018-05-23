package com.library.core.util;

//  Created by ruibing.han on 2018/3/27.

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSonUtil {
    //屏蔽new 创建和反射创建
    private GSonUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Gson gson;

    public static Gson defaultGSon() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy/MM/dd HH:mm:ss")
                    .create();
        }
        return gson;
    }
}

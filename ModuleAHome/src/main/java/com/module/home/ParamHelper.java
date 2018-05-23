package com.module.home;
/*
 * Created by Ruibing.han on 2018/5/21.
 */

import com.library.common.module.http.helper.En_DecryptionHelper;

import java.util.HashMap;
import java.util.Map;

public class ParamHelper {
    private static ParamHelper instance;

    public static ParamHelper getInstance() {
        if (instance == null) {
            synchronized (ParamHelper.class) {
                if (instance == null) {
                    instance = new ParamHelper();
                }
            }
        }
        return instance;
    }

    private ParamHelper() {
    }

    /**
     * 临时保存的参数Map
     */
    private Map<String, String> params;

    /**
     * 版本号
     */
    public final static String APP_VERSION = "2.0.1";

    /**
     * 语言
     */
    public final static String APP_LANG = "zh-Hans";

    public static final int HOME_LOGIN = 1;

    public Map<String, String> getHomeParam(int paramType, String... paramArray) {
        if (params == null)
            params = new HashMap<>();
        else params.clear();
        switch (paramType) {
            case HOME_LOGIN://登录
                params.put("loginName", paramArray[0]);
                params.put("password", paramArray[1]);
                params.put("brandCode", paramArray[2]);
                break;
        }

        params.put("lang", APP_LANG);
        params.put("apiVersion", APP_VERSION);
        try {
            return En_DecryptionHelper.getInstance().sortMapByKey(params);
        } catch (Exception ignored) {
        }
        return null;
    }
}

package com.library.common.app;

//  Created by ruibing.han on 2018/5/15.

import com.alibaba.android.arouter.launcher.ARouter;
import com.library.common.component.service.AppIntentService;
import com.library.core.base.BaseApplication;
import com.library.core.module.auto.config.AutoLayoutConfig;

public class MyApplication extends BaseApplication {

    private static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    //在onCreate方法中进行其他的初始化操作，如数据库，更改时区(需要在主线程执行的初始化)
    @Override
    protected void onCreateInit() {
        mInstance = this;

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(mInstance); // 尽可能早，推荐在Application中初始化

        AutoLayoutConfig.getInstance().init(mInstance);

    }

    //activity生命周期回调监听
    @Override
    protected void onInitActivityLifecycleCallbacks() {

    }

    //监听内存使用情况
    @Override
    protected void onInitComponentStorageCallbacks() {

    }

    //通过IntentService初始化第三方（友盟分享，极光推送，第三方支付等））
    @Override
    protected void onInitIntentService() {
        AppIntentService.initService(getInstance());
    }
}

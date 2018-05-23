package com.library.common.component.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;


//  Created by ruibing.han on 2018/3/30.

public class AppIntentService extends IntentService {
    public final static String INTENT_ACTION = "appInitService";

    public AppIntentService() {
        super("appIntentService");
    }

    public static void initService(Context context) {
        Intent intentService = new Intent(context, AppIntentService.class);
        intentService.setAction(INTENT_ACTION);
        context.startService(intentService);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //65535方法限制处理的初始化
        //MultiDex.install(this);

        //AutoLayout屏幕适配
        //AutoLayoutConfig.getInstance().useDeviceSize().init(this);

        //初始化数据库
        //GreenDaoManager.getInstance();

        //Logger打印日志的初始化（添加日志附加条件）
        // Logger.addLogAdapter(new AndroidLogAdapter() {
//            @Override
//            public boolean isLoggable(int priority, String tag) {
//                return BuildConfig.DEBUG;
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.shop.dog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.library.common.module.router.ARouterConstant;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //根据登录页面的路由，进入登录页面
        ARouter.getInstance().build(ARouterConstant.ROUTER_HOME_LOGIN).navigation();
        finish();
    }
}

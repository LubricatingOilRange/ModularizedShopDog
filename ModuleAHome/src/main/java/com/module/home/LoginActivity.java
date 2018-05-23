package com.module.home;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.library.common.entry.UserCommand;
import com.library.common.module.http.api.AppService;
import com.library.common.module.http.helper.ApiHelper;
import com.library.common.module.router.ARouterConstant;
import com.library.common.module.window.LoadingDialog;
import com.library.core.base.activity.BaseInputActivity;
import com.library.core.module.http.helper.RxJavaHelper;
import com.library.core.module.http.rxjava.CommonSubscriber;
import com.library.core.util.LogUtil;
import com.library.core.util.SPUtil;

import java.util.Map;

@Route(path = ARouterConstant.ROUTER_HOME_LOGIN)
public class LoginActivity extends BaseInputActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreateInit() {
        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
                Map<String, String> params = ParamHelper.getInstance().getHomeParam(ParamHelper.HOME_LOGIN,
                        "666",//loginName
                        "111111aA", //password
                        "BLUEBOX");//brandCode
                AppService appService = ApiHelper.getInstance().createAppService();
                appService.login(params)
                        .compose(RxJavaHelper.rxSchedulerHelper())
                        .compose(RxJavaHelper.handleBaseResponseResult())
                        .subscribe(new CommonSubscriber<UserCommand>(null) {

                            @Override
                            protected void onStart() {
                                super.onStart();
                                loadingDialog.show();
                            }

                            @Override
                            public void onNext(UserCommand userCommand) {
                                LogUtil.d("onNext");
                                SPUtil.putObject(LoginActivity.this, SPUtil.SP_USER_COMMAND, userCommand);
                                ARouter.getInstance()
                                        .build(com.library.common.module.router.ARouterConstant.ROUTER_HOME_MAIN)//主页面路由
                                        .withParcelable(SPUtil.SP_USER_COMMAND, userCommand)
                                        .navigation();
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                                loadingDialog.dismiss();
                                finish();
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            moveTaskToBack(false);
        } else {
            super.onBackPressed();
        }
    }
}

package com.module.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.library.common.entry.UserCommand;
import com.library.common.module.router.ARouterConstant;
import com.library.core.util.SPUtil;

@Route(path = ARouterConstant.ROUTER_HOME_MAIN)
public class MainActivity extends AppCompatActivity {

    @Autowired(name = SPUtil.SP_USER_COMMAND)
    UserCommand userCommand;    // 支持解析自定义对象，URL中使用json传递

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ARouter.getInstance().inject(this);
        TextView tv_login = findViewById(R.id.tv_login);
        tv_login.setText(userCommand.getStoreName());
    }

    //退出 进入后台
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            moveTaskToBack(false);
        } else {
            super.onBackPressed();
        }
    }
}

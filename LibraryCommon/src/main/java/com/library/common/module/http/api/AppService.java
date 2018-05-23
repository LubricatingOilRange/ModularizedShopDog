package com.library.common.module.http.api;
/*
 * Created by Ruibing.han on 2018/5/21.
 */

import com.library.common.entry.UserCommand;
import com.library.core.module.http.response.BaseResponse;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface AppService {

    String HOST = "http://hubforeign-test.mapemall.com/";//新加波测试环境

    //登陆页面
    @POST("shopdog/auth/login")
    Flowable<BaseResponse<UserCommand>> login(@QueryMap Map<String, String> map);
}

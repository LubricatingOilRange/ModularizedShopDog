package com.library.common.module.http.helper;
/*
 * Created by Ruibing.han on 2018/5/21.
 */

import com.library.common.app.MyApplication;
import com.library.common.module.http.api.AppService;
import com.library.common.module.http.encrypt.DecodeConverterFactory;
import com.library.core.module.http.helper.OkHttpHelper;
import com.library.core.module.http.helper.RetrofitHelper;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ApiHelper {
    private static ApiHelper instance;

    //    private OtherService mOtherService;
    private AppService mAppService;

    private Retrofit mRetrofit;

    private ApiHelper() {
    }

    public static ApiHelper getInstance() {
        if (instance == null) {
            synchronized (ApiHelper.class) {
                if (instance == null) {
                    instance = new ApiHelper();
                }
            }
        }
        return instance;
    }

    //获取AppService对象
    public AppService getAppService() {
        if (mAppService == null) {
            mAppService = createAppService();
        }
        return mAppService;
    }

    public void setAppService(AppService mAppService) {
        this.mAppService = mAppService;
    }

    public AppService createAppService() {
        initRetrofit();
        return mRetrofit.create(AppService.class);

    }

    //获取OtherService对象（临时使用）
//    public OtherService getOtherService() {
//        if (mOtherService == null) {
//            initRetrofit();
//            return mRetrofit.create(OtherService.class);
//        }
//        return mOtherService;
//    }
//
//    public void cancelOtherService() {
//        if (mOtherService != null) {
//            mOtherService = null;
//        }
//    }


    //初始化retrofit对象
    private void initRetrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder okHttpClientBuilder = OkHttpHelper.getOkHttpClientBuilder();
            OkHttpClient okHttpClient = OkHttpHelper.getOkHttpClient(MyApplication.getInstance(), okHttpClientBuilder);
            Retrofit.Builder retrofitBuilder = RetrofitHelper.getRetrofitBuilder();
            mRetrofit = RetrofitHelper.getRetrofit(retrofitBuilder,
                    okHttpClient,
                    AppService.HOST,
                    DecodeConverterFactory.create());
        }
    }
}

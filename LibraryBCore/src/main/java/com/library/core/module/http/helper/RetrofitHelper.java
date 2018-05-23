package com.library.core.module.http.helper;

import com.library.core.util.GSonUtil;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private RetrofitHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //获取RetrofitBuilder
    public static Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    /**
     * 获取Retrofit
     *
     * @param factory (RxJava2CallAdapterFactory,DecodeConverterFactory 解密)
     */
    public static Retrofit getRetrofit(Retrofit.Builder builder, OkHttpClient client, String url, Converter.Factory factory) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build();
    }
}

package com.library.core.module.http.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.library.core.BuildConfig;
import com.library.core.util.FileUtil;
import com.library.core.util.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpHelper {

    private OkHttpHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //获取OkHttpClientBuilder
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    //获取OkHttpClient
    public static OkHttpClient getOkHttpClient(final Context context, OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {//当前属于测试
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        File cacheFile;
        if (FileUtil.isMountedSDCard()) {//判断是否有SD卡
            cacheFile = new File(FileUtil.CACHE_PATH + File.separator + "netCache");
        } else {
            cacheFile = new File(context.getCacheDir() + File.separator + "netCache");
        }
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetWorkUtil.isConnected(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetWorkUtil.isConnected(context)) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
//        设置统一的请求头部参数
//        Interceptor apiKey = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                request = request.newBuilder()
//                        .addHeader("apiKey",Constants.KEY_API)
//                        .build();
//                return chain.proceed(request);
//            }
//        }
//        builder.addInterceptor(apikey);
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        return builder.build();
    }
}

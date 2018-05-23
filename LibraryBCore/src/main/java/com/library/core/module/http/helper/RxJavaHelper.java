package com.library.core.module.http.helper;

import com.library.core.module.http.exception.AppException;
import com.library.core.module.http.response.BaseResponse;

import org.reactivestreams.Publisher;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxJavaHelper {

    private RxJavaHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /*
     * 统一线程处理调度
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /*
     * 没有基类  直接处理返回结果
     *
     * @param <T>
     */
    public static <T> FlowableTransformer<T, T> handleResult() {   //compose判断结果
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> response) {
                return response.flatMap(new Function<T, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(T t) throws Exception {
                        if (t != null) {
                            return createData(t);
                        } else {
                            return Flowable.error(new AppException("服务器异常", "-1"));
                        }
                    }
                });
            }
        };
    }

    /*
     * 统一返回结果处理 返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<BaseResponse<T>, T> handleBaseResponseResult() {   //compose判断结果
        return new FlowableTransformer<BaseResponse<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<BaseResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<BaseResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(BaseResponse<T> resultResponse) {
                        if (resultResponse.getData() != null) {
                            return createData(resultResponse.getData());
                        } else {
                            return Flowable.error(new AppException("服务器异常", "-1"));
                        }
                    }
                });
            }
        };
    }

    /*
     * 自定义 -- 创建
     *
     * @param <T>
     * @return
     */
    private static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);//BUFFER--缓存策略，LATEST,DROP--需要什么就发射什么数据
    }
}

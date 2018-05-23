package com.library.core.module.http.rxjava.flowable;

import com.library.core.util.LogUtil;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

// RxJava2 的组合类型操作符的使用
public class RxFlowGroupUtil {
    /*
     * CombineLatest(连个Flowabel进行组合，规则进行处理后进行发射)
     */

    /**
     * 只有当两个observable都发射过第一项数据时，才能进行组合发射
     * CombineLatest
     * 当两个Observables中的任何一个发射了一个数据时，将两个Observables数据通过指定的规则进行处理，将结果进行发射
     */
    public static void combineLatest() {
        Flowable<Long> flow1 = Flowable.interval(0, 50, TimeUnit.MILLISECONDS).take(3);
        Flowable<Long> flow2 = Flowable.interval(50, 50, TimeUnit.MILLISECONDS).take(3);

        Flowable.combineLatest(flow1, flow2, new BiFunction<Long, Long, Long>() {
            @Override
            public Long apply(Long integer1, Long integer2) throws Exception {
                LogUtil.d("integer1:" + integer1 + " --- integer2:" + integer2);
                return integer1 + integer2;
            }
        })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long integer) throws Exception {
                        LogUtil.d("combineLatest:" + integer);
                    }
                });
        /*
         * 显示结果
         * 1 + 0 = 1   -  0 （0）, 500 （1），1000（2）
         * 1 + 1 = 2   -  500（0）,1000（1）,1500（2）
         * 2 + 1 = 3
         * 2 + 2 = 4
         */
    }

    /**
     * 无论何时，如果一个Observable发射了一个数据项，只要在另一个Observable发射的数据项定义的时间窗口内，就将两个Observable发射的数据合并发射
     */
    public static void join() {
        Flowable<Long> flow1 = Flowable.interval(0, 1, TimeUnit.SECONDS).take(3);
        Flowable<Long> flow2 = Flowable.interval(1, 1, TimeUnit.SECONDS).take(3);

        flow2.join(flow1, new Function<Long, Publisher<Long>>() {
            @Override
            public Publisher<Long> apply(Long aLong) throws Exception {
                LogUtil.i("apply1:" + aLong);
                return Flowable.just(aLong);
            }
        }, new Function<Long, Publisher<Long>>() {
            @Override
            public Publisher<Long> apply(Long aLong) throws Exception {
                LogUtil.i("apply2:" + aLong);
                return Flowable.just(aLong);
            }
        }, new BiFunction<Long, Long, String>() {
            @Override
            public String apply(Long aLong1, Long aLong2) throws Exception {
                LogUtil.i("aLong1：" + aLong1 + "   ---aLong2" + aLong2);
                return "数据->:" + (aLong1 + aLong2);
            }
        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.i("join:" + s);
                    }
                });
    }
}

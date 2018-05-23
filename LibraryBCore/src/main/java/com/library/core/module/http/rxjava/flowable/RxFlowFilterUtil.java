package com.library.core.module.http.rxjava.flowable;

import com.library.core.util.LogUtil;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/*
 * Created by ruibing.han on 2017/12/25.
 */

// RxJava2 的过滤类型操作符的使用
public class RxFlowFilterUtil {
    /*
     * deBounce（）
     * distinct（去除重复）
     * ElementAt(取指定位置的值)
     * filter(条件过滤)
     * First(第一个数据 可设置默认)
     * Last(最后一个数据 可设置默认值)
     * IgnoreElements（忽略所有数据--上游不接收，只接收onError或onComplete）
     * Sample(定期扫描Flowable中的数据)
     * skip(跳过前面几个数据)
     * SkipLast(跳过后面几个数据)
     * Take(只保留前几个数据)
     * takeLast(只保留后几个数据)
     */

    /**
     * deBounce(条件)
     * 源Flowable产出一个结果时开始计时，如果在规定的间隔时间内没有别的结果产生
     * 或者在此期间调用了onCompleted，则发射数据，否则忽略发射
     */
    public static void deBounce() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                try {
                    for (int i = 0; i < 10; i++) {
                        emitter.onNext("emitter:" + i);
                        Thread.sleep(100 * i);
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.DROP)
                .debounce(400, TimeUnit.MILLISECONDS)//每次发射数据间的间隔超过300ms
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.d("deBounce:" + s);
                    }
                });

        //数据结果是 5,6,7,8,9
    }


    /*
     * distinct(去重复)
     * 去除重复数据，进行发射
     */
    public static void distinct() {
        Flowable.just(1, 5, 2, 4, 1, 4, 5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("distinct:" + integer);
                    }
                });
        //数据结果 ： 1,5,2,4
    }


    /*
     * elementAt(取值)
     * 取值，取特定位置的数据项，索引是从0开始的
     */
    public static void elementAt() {
        Flowable.just(1, 2, 45, 23, 204)
                .elementAt(3) // ---- 23
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("ElementAt:" + integer);
                    }
                });
    }

    /**
     * filter
     * 对发射的数据进行过滤，只发射符合条件的数据~
     */
    public static void filter() {
        Flowable.just(1, 4, 7, 10, 23, 50)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer >= 7;//----7,10,23,50
                    }
                })
                .compose(new FlowableTransformer<Integer, Integer>() {
                    @Override
                    public Publisher<Integer> apply(Flowable<Integer> integerFlowable) {
                        return integerFlowable.filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer integer) throws Exception {
                                return integer < 50;// ---- 7,10,23
                            }
                        });
                    }
                })
                // .elementAt(2) // ---- 23
                .flatMap(new Function<Integer, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(Integer integer) throws Exception {
                        LogUtil.d("map:" + integer);
                        return Flowable.just("数据:" + integer);//转换数据类型为String
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.d(s);
                    }
                });
    }

    /*
     * 当没有第一个元素时 取默认值15
     */
    public static void first() {
        Flowable.fromIterable(new ArrayList<Integer>())
                .first(15)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("first:" + integer);
                    }
                });
    }

    /*
     * 当没有最后一个元素时 取默认值28
     */
    public static void last() {
        Flowable.fromIterable(new ArrayList<Integer>())
                .last(28)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object integer) throws Exception {
                        LogUtil.d("first:" + integer);
                    }
                });

    }


    /*
     * 忽略所有数据--上游不接收，只接收onError或onComplete
     */
    public static void ignoreElements() {
        Flowable.just(1, 2, 4, 5, 6)
                .ignoreElements()
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.d("ignoreElements:" + "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d("ignoreElements:" + "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("ignoreElements:" + "onError");
                    }
                });
    }

    /**
     * sample(sa)
     */
    public static void example() {
        Flowable.interval(100, TimeUnit.MILLISECONDS)
                .sample(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("example:" + aLong);
                    }
                });
        /*
         * 显示结果：0,2,4,6,8,10
         */
    }

    /**
     * skip
     * 跳过前面的几个数据
     */
    public static void skip() {
        Flowable.just(1, 2, 3, 4, 5, 6, 7)
                .skip(2)// 不是索引
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("skip:" + integer);
                    }
                });
        /*
         * 显示结果：3，4，5，6，7
         */
    }

    /**
     * skip
     * 跳过前面的几个数据
     */
    public static void skipLast() {
        Flowable.just(1, 2, 3, 4, 5, 6, 7)
                .skipLast(2)// 不是索引
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("skipLast:" + integer);
                    }
                });
        /*
         * 显示结果：1,2,3,4,5
         */
    }

    /**
     * 只保留前几个数据
     */
    public static void take() {
        Flowable.just(1, 2, 3, 4, 5, 6)
                .take(2)//不是索引
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("take:" + integer);
                    }
                });
        /*
         * 显示结果：1,2
         */
    }

    /**
     * 只保留前几个数据
     */
    public static void takeLast() {
        Flowable.just(1, 2, 3, 4, 5, 6)
                .takeLast(2)//不是索引
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("takeLast:" + integer);
                    }
                });
        /*
         * 显示结果：5,6
         */
    }
}

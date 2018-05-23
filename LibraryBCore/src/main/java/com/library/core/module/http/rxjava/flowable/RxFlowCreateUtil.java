package com.library.core.module.http.rxjava.flowable;
import android.app.Activity;

import com.library.core.util.LogUtil;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Consumer;

/*
 * RxJava2.X中，Observeable用于订阅Observer，是不支持背压的，而Flowable用于订阅Subscriber，是支持背压(Backpressure)的
 */

//RxJava2 的创建类型操作符的使用

/**
 *
 */
public class RxFlowCreateUtil {
    /*
     * create
     * just
     * fromArray
     * fromIterable
     * defer -- 订阅后才创建一个Flowable,每次订阅都创建一个新的Flowable
     * range --从100 执行 五次 100- 104
     * timer -- 从0开始2s后执行
     * delay -- 对数据进行延迟发射数据
     * interval -- 间隔2s 发射数据
     * repeat  -- 对数据重复多少次
     * empty -- 创建一个空的Flowable,会执行onComplete()就终止
     * never -- 创建一个不发射任何数据的Flowable
     * error -- 创建一个Error的Flowable,会执行onError()就结束
     */

    /**
     * Flowable中的create创建操作符的使用
     * BackpressureStrategy：
     * Buffer(全部缓存--缓存策略（换更大的缓存池 超过128）),
     * LATEST,DROP（接受到最后一个事件）--需要什么就发射什么数据,
     * ERROR(超过128 就报异常),
     * MISSING( 超过128个就丢弃)
     */
    public static void create() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                try {
                    emitter.onNext("a");
                    emitter.onNext("b");
                    emitter.onNext("c");
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(new FlowableSubscriber<String>() {
                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(String t) {
                        LogUtil.d("create:" + t);
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        subscription.cancel();//取消订阅
                    }

                    @Override
                    public void onComplete() {
                        subscription.cancel();//取消订阅
                    }
                });
    }

    /**
     * Flowable中的just创建操作符的使用
     */
    public static void just() {
        Flowable.just("a", "b")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.d("just：" + s);
                    }
                });
    }

    /**
     * Flowable中的fromArray创建操作符的使用
     */
    public static void fromArray() {
        String[] array = new String[]{"a", "b", "c"};
        Flowable.fromArray(array)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.d("fromArray：" + s);
                    }
                });
    }

    /**
     * Flowable中的fromIterable创建操作符的使用
     */
    public static void fromIterable() {
        List<String> dataList = new ArrayList<>();
        dataList.add("a");
        dataList.add("b");
        dataList.add("c");
        Flowable.fromIterable(dataList)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.d("fromIterable：" + s);
                    }
                });
    }

    /**
     * defer操作符只有当有订阅者Subscriber来订阅的时候才会创建一个新的Observable对象,
     * 多次订阅该Flowable都是不同的 ---- 每次订阅都会创新一个新的Flowable
     */
    public static void defer() {
        Flowable.defer(new Callable<Publisher<String>>() {
            @Override
            public Publisher<String> call() throws Exception {
                return Flowable.just("a", "b", "c");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.d("defer：" + s);
            }
        });
    }

    /**
     * Flowable中的range创建操作符的使用--从某个数开始执行多少次
     * 从100开始执行10次 ---- 100 - 109
     */
    public static void range() {
        Flowable.range(100, 10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("range：" + integer);
                    }
                });
    }

    /**
     * Flowable中的timer创建操作符的使用--从 0 延迟time 时间后执行
     * 延迟10s 后发射数据
     */
    public static void timer(Activity activity) {
        Flowable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.d("timer：" + aLong);
                        activity.finish();
                    }
                });
    }

    /**
     * 效果跟timer一样 延迟发射数据（delay针对了数据源）
     */
    public static void delay() {
        Flowable.just("a", "b", "c")
                .delay(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String delay) throws Exception {
                        LogUtil.d("delay：" + delay);
                    }
                });
    }

    /**
     * Flowable中的interval创建操作符的使用 -- 无限执行，从0开始每隔固定的时间发射一次数据
     * 每隔2s发射一次数据，订阅者接受到的数据time是累加的，0,1,2,3,4.....
     */
    public static void interval() {
        Flowable.interval(2000, 1000, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long time) throws Exception {
                        LogUtil.d("interval：" + time);
                    }
                });
    }

    /**
     * Repeat会将一个Flowable对象重复发射，接收值是发射的次数，依然订阅在 computation Scheduler
     * 重复发送 10 次数据 1
     */
    public static void repeat() {
        Flowable.just(1)
                .delay(2, TimeUnit.SECONDS)
                .repeat(10)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("repeat：" + integer);
                    }
                });
    }

    /**
     * 创建一个Flowable不发射任何数据(onNext)、而是立即调用onCompleted方法终止~
     */
    public static void empty() {
        Flowable.empty().subscribe(new FlowableSubscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Object o) {
                LogUtil.d("empty：" + "onNext");
            }

            @Override
            public void onError(Throwable t) {
                LogUtil.d("empty：" + "onError");
            }

            @Override
            public void onComplete() {
                LogUtil.d("empty：" + "onComplete");
            }
        });
    }

    /**
     * 创建一个Flowable不发射任何数据、也不给订阅ta的subscriber发出任何通知
     */
    public static void never() {
        Flowable.never().subscribe(new FlowableSubscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Object o) {
                LogUtil.d("empty：" + "onNext");
            }

            @Override
            public void onError(Throwable t) {
                LogUtil.d("empty：" + "onError");
            }

            @Override
            public void onComplete() {
                LogUtil.d("empty：" + "onComplete");
            }
        });
    }

    /**
     * 创建一个Flowable不发射任何数据、也不给订阅ta的subscriber发出任何通知
     */
    public static void error() {
        Flowable.error(new Callable<Throwable>() {
            @Override
            public Throwable call() throws Exception {
                return new Exception("系统异常");
            }
        }).subscribe(new FlowableSubscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Object o) {
                LogUtil.d("empty：" + "onNext");
            }

            @Override
            public void onError(Throwable t) {
                LogUtil.d("empty：" + "onError-" + t.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtil.d("empty：" + "onComplete");
            }
        });
    }
}

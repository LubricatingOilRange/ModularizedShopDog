package com.library.core.module.http.rxjava.flowable;

import com.library.core.util.LogUtil;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/*
 * Created by ruibing.han on 2017/12/22.
 */
//RxJava2 的转换类型操作符的使用
public class RxFlowTransformUtil {
    /*
     * Buffer(2,数据) 缓存后每次发射两个数据，直到没有数据
     * Map (1 对1)
     * FlatMap 无序（1 对 1，多 对 多）
     * concatMap 有序（1 对 1，多 对 多）
     * GroupBy (分组)
     * Scan （累加）
     * Window （Buffer,Flowable（被观察者））
     */

    /**
     * buffer缓存的意思:将12345进行缓存后 每次发射2个
     */
    public static void buffer() {
        Flowable.just(1, 2, 3, 4, 5)
                .buffer(2)//一次发射2个
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        LogUtil.d("buffer" + integers.toString());
                    }
                });
    }

    /*
     * 一对一，1 - 1，2 - 2
     * 上游接收到一个数据1 就调用一次onNext(),在这调用了三次
     */
    public static void map() {
        Flowable.just(1, 2, 3)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        LogUtil.d("map:" + integer);
                        return "数据：" + integer;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.d("map:" + s);
                    }
                });
    }

    /**
     * 一对多，多对多（无序的）
     * 1,2,3 -- 1,2,3（存在1，3，2）(上游将123进行保存后 连续发送数据给订阅者 -》 123) 只调用onNext()方法一次
     */
    public static void flatMap() {
        Flowable.just("1", "2", "3")
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(final String dataString) throws Exception {
                        LogUtil.d("flatMap：" + dataString);
                        return Flowable.create(new FlowableOnSubscribe<String>() {
                            @Override
                            public void subscribe(FlowableEmitter<String> e) throws Exception {
                                try {
                                    e.onNext("数据：" + dataString);
                                    e.onComplete();
                                } catch (Exception e1) {
                                    e.onError(e1);
                                }
                            }
                        }, BackpressureStrategy.BUFFER);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.d("flatMap：" + s);
                    }
                });
    }

    /**
     * 一对多，多对多(相对与flagMap来说，concatMap是有序的)
     * 1，2，3 -- 接收到的数据一定是1，2，3
     */
    public static void concatMap() {
        Flowable.just(1, 2, 3)
                .concatMap(new Function<Integer, Publisher<List<String>>>() {
                    @Override
                    public Publisher<List<String>> apply(Integer integer) throws Exception {
                        List<String> strings = new ArrayList<>();
                        return Flowable.just(strings);
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> s) throws Exception {
                        LogUtil.d("数据：" + s);
                    }
                });

    }

    /**
     * 一对一  每次发射一个数据都将其分组判断
     */
    public static void groupBy() {
        Flowable.range(0, 5)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        LogUtil.d("groupBy:" + integer);
                        return integer % 2 == 0 ? "A" : "B";//分组的条件判断
                    }
                })
                .subscribe(new Consumer<GroupedFlowable<String, Integer>>() {
                    @Override
                    public void accept(final GroupedFlowable<String, Integer> stringIntegerGroupedFlowable) throws Exception {
                        stringIntegerGroupedFlowable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                //分组的组名和发射的数据
                                LogUtil.d("Group -->" + stringIntegerGroupedFlowable.getKey() + "  &&  OnNext -->" + integer);
                            }
                        });
                    }
                });
    }

    /**
     * 累加 (current值从0 - 3，last前一次累加值)
     * last + current = scan
     * 0  +    0    =  0
     * 0  +    1    =  1
     * 1  +    2    =  3
     * 3  +    3    =  6
     * 6  +    4    =  10
     */
    public static void scan() {
        Flowable.range(0, 5)
                // last,current,返回值
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    /**
                     * @param last    (上一次结算值)
                     * @param current （当前的数值）
                     */
                    @Override
                    public Integer apply(Integer last, Integer current) throws Exception {
                        LogUtil.d("last:" + last + " --current:" + current);
                        return last + current;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.d("scan:" + integer);
                    }
                });
    }

    /*
     * 窗口，它可以批量或者按周期性从Observable收集数据到一个集合，
     * 然后把这些数据集合打包发射，而不是一次发射一个数据,类似于Buffer，
     * 但Buffer发射的是数据，Window发射的是Observable~
     */
    public static void window() {
        Flowable.range(1, 5)
                .window(2)//每次发射 2 个数据
                .subscribe(new Consumer<Flowable<Integer>>() {
                    @Override
                    public void accept(Flowable<Integer> integerFlowable) throws Exception {
                        LogUtil.d("Flowable:" + integerFlowable.toString());
                        integerFlowable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                LogUtil.d("window:" + integer);
                            }
                        });
                    }
                });

        /* 数据返回格式
         *  integerFlowable-1      1 , 2
         *  integerFlowable-2      3 , 4
         *  integerFlowable-3      5
         */
    }
}

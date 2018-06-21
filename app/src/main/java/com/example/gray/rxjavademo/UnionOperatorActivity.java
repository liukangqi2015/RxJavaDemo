package com.example.gray.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gray on 2018/1/29.
 * RxJava合并操作符页面
 */

public class UnionOperatorActivity extends AppCompatActivity {
    private static final String TAG = "UnionOperatorActivity";

    @BindArray(R.array.union_operator_subtitle)
    String[] subtitles;
    @BindView(R.id.subtitle_02)
    TextView subtitle02;
    @BindView(R.id.concat_btn)
    Button concatBtn;
    @BindView(R.id.subtitle_03)
    TextView subtitle03;
    @BindView(R.id.merge_btn)
    Button mergeBtn;
    @BindView(R.id.subtitle_04)
    TextView subtitle04;
    @BindView(R.id.concatDelayError_btn)
    Button concatDelayErrorBtn;
    @BindView(R.id.subtitle_05)
    TextView subtitle05;
    @BindView(R.id.zip_btn)
    Button zipBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        subtitle02.setText(subtitles[0]);
        subtitle03.setText(subtitles[1]);
        subtitle04.setText(subtitles[2]);
        subtitle05.setText(subtitles[3]);
    }


    @OnClick({R.id.concat_btn, R.id.merge_btn, R.id.concatDelayError_btn, R.id.zip_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.concat_btn:
                concatOperator();
                break;
            case R.id.merge_btn:
                merge();
                break;
            case R.id.concatDelayError_btn:
                concatDelayError();
                break;
            case R.id.zip_btn:
                zip();
                break;
        }
    }

    /**
     * concat（） / concatArray（）
     * 组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
     * <p>
     * 二者区别：组合被观察者的数量，即concat（）组合被观察者数量≤4个，而concatArray（）则可＞4个
     */
    private void concatOperator() {
        final StringBuilder sb = new StringBuilder();
        // concat（）：组合多个被观察者（≤4个）一起发送数据
        // 注：串行执行
        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6), Observable.just(7, 8, 9))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                        sb.append(value);
                        sb.append("\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                        concatBtn.setText(sb);
                    }
                });

        // concatArray（）：组合多个被观察者一起发送数据（可＞4个）
        // 注：串行执行
//        Observable.concatArray(Observable.just(1, 2, 3),
//                Observable.just(4, 5, 6),
//                Observable.just(7, 8, 9),
//                Observable.just(10, 11, 12),
//                Observable.just(13, 14, 15))
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer value) {
//                        Log.d(TAG, "接收到了事件"+ value  );
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "对Error事件作出响应");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "对Complete事件作出响应");
//                    }
//                });


    }

    /**
     * merge（） / mergeArray（）
     * 组合多个被观察者一起发送数据，合并后 按时间线并行执行
     * 二者区别：组合被观察者的数量，即merge（）组合被观察者数量≤4个，而mergeArray（）则可＞4个
     */
    private void merge() {
        // merge（）：组合多个被观察者（＜4个）一起发送数据
        // 注：合并后按照时间线并行执行
        Observable.merge(
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), // 从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(2, 3, 1, 1, TimeUnit.SECONDS)) // 从2开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });

        // mergeArray（） = 组合4个以上的被观察者一起发送数据，此处不作过多演示，类似concatArray（）


    }

    /**
     * concatDelayError（） / mergeDelayError（）
     * 若其中一个被观察者发出onError()，不会终止其他的被观察者发送事件。
     * concat与merge则会
     */
    private void concatDelayError() {
        Observable.concatArrayDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onError(new NullPointerException()); // 发送Error事件，因为使用了concatDelayError，所以第2个Observable将会发送事件，等发送完毕后，再发送错误事件
                        emitter.onComplete();
                    }
                }),
                Observable.just(4, 5, 6))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * Zip（）
     * 合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列），并最终发送
     */
    private void zip() {
//        <-- 创建第1个被观察者 -->
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "被观察者1发送了事件1");
                emitter.onNext(1);
                // 为了方便展示效果，所以在发送事件后加入1s的延迟
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件3");
                emitter.onNext(3);
                Thread.sleep(1000);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()); // 设置被观察者1在工作线程1中工作

//        <-- 创建第2个被观察者 -->
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "被观察者2发送了事件A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件D");
                emitter.onNext("D");

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());// 设置被观察者2在工作线程2中工作
        // 假设不作线程控制，则该两个被观察者会在同一个线程中工作，即发送事件存在先后顺序，而不是同时发送

//        <-- 使用zip变换操作符进行事件合并 -->
// 注：创建BiFunction对象传入的第3个参数 = 合并后数据的数据类型
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String string) throws Exception {
                return integer + string;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "最终接收到的事件 =  " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });


    }


}

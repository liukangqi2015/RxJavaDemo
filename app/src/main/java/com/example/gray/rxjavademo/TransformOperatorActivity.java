package com.example.gray.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Gray on 2018/1/23.
 * RxJava变换操作符
 */

public class TransformOperatorActivity extends AppCompatActivity {
    private static final String TAG = "TransformOperator";
    @BindArray(R.array.transform_operator_subtitle)
    String[] subtitles;
    @BindView(R.id.subtitle_02)
    TextView subtitle02;
    @BindView(R.id.map_btn)
    Button mapBtn;
    @BindView(R.id.subtitle_03)
    TextView subtitle03;
    @BindView(R.id.flatMap_btn)
    Button flatMap_btn;
    @BindView(R.id.subtitle_04)
    TextView subtitle04;
    @BindView(R.id.concatMap_btn)
    Button concatMap_btn;
    @BindView(R.id.subtitle_05)
    TextView subtitle05;
    @BindView(R.id.buffer_btn)
    Button buffer_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform_operator);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        subtitle02.setText(subtitles[0]);
        subtitle03.setText(subtitles[1]);
        subtitle04.setText(subtitles[2]);
        subtitle05.setText(subtitles[3]);
    }

    @OnClick({R.id.map_btn, R.id.flatMap_btn, R.id.concatMap_btn, R.id.buffer_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map_btn:
                map();
                break;
            case R.id.flatMap_btn:
                flatMap();
                break;
            case R.id.concatMap_btn:
                concatMap();
                break;
            case R.id.buffer_btn:
                buffer();
                break;
        }
    }

    /**
     * 对 被观察者发送的每1个事件都通过 指定的函数 处理，从而变换成另外一种事件。
     * 即， 将被观察者发送的事件转换为任意的类型事件。
     */
    private void map() {
        final StringBuilder sb = new StringBuilder();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                String str = "使用map操作符将事件" + integer + "由 " + integer.getClass().getSimpleName() + integer
                        + "转换成 Sting" + integer;
                return str;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                sb.append(value);
                sb.append("\n");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mapBtn.setText(sb);
            }
        });
    }

    /**
     * 将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送.
     * 新的事件序列是无序的,但是和事件发送线程有关
     */
    private void flatMap() {
        final StringBuilder sb = new StringBuilder();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onComplete();
            }

            // 采用flatMap（）变换操作符
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    list.add("我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.e(TAG, value);
                sb.append(value);
                sb.append("\n");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                flatMap_btn.setText(sb);
            }
        });
    }

    /**
     * 与FlatMap（）的 区别在于：拆分 & 重新合并生成的事件序列 的顺序 = 被观察者旧序列生产的顺序
     */
    private void concatMap() {
        final StringBuilder sb = new StringBuilder();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onComplete();
            }

            // 采用flatMap（）变换操作符
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    list.add("我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过concatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.e(TAG, value);
                sb.append(value);
                sb.append("\n");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                concatMap_btn.setText(sb);
            }
        });
    }

    /**
     * 定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
     */
    private void buffer() {
        final StringBuilder sb=new StringBuilder();
        // 被观察者 需要发送5个数字
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 1) // 设置缓存区大小 & 步长
                // 缓存区大小 = 每次从被观察者中获取的事件数量
                // 步长 = 每次获取新事件的数量
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List<Integer> stringList) {
                        Log.d(TAG, " 缓存区里的事件数量 = " +  stringList.size());
                        sb.append(" 缓存区里的事件数量 = " +  stringList.size()+"\n");
                        for (Integer value : stringList) {
                            Log.d(TAG, " 事件 = " + value);
                            sb.append(" 事件 = " + value+"\n");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应" );
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                        buffer_btn.setText(sb);
                    }
                });
    }
}

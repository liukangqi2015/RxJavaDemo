package com.example.gray.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.example.gray.rxjavademo.R.id.subtitle_01;

/**
 * Created by Gray on 2017/11/23.
 * 演示创建操作符
 */

public class CreateOperatorActivity extends AppCompatActivity {
    @BindArray(R.array.create_operator_subtitle)
    String[] subtitles;
    @BindView(subtitle_01)
    TextView subtitle01;
    @BindView(R.id.subtitle_02)
    TextView subtitle02;
    @BindView(R.id.just_btn)
    Button justBtn;
    @BindView(R.id.subtitle_03)
    TextView subtitle03;
    @BindView(R.id.fromArray_btn)
    Button fromArrayBtn;
    @BindView(R.id.subtitle_04)
    TextView subtitle04;
    @BindView(R.id.defer_btn)
    Button deferBtn;
    @BindView(R.id.subtitle_05)
    TextView subtitle05;
    @BindView(R.id.timer_btn)
    Button timerBtn;
    @BindView(R.id.subtitle_06)
    TextView subtitle06;
    @BindView(R.id.interval_btn)
    Button intervalBtn;


    private static final String TAG = "CreateOperatorActivity";
    private StringBuilder sb = new StringBuilder();
    private Integer[] nums = new Integer[]{1, 2, 3, 4};
    private StringBuilder nums_sb = new StringBuilder("数组中的元素");
    private Integer i = 10;
    private Observer<Long> observer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_operator);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        subtitle01.setText(subtitles[0]);
        subtitle02.setText(subtitles[1]);
        subtitle03.setText(subtitles[2]);
        subtitle04.setText(subtitles[3]);
        subtitle05.setText(subtitles[4]);
        subtitle06.setText(subtitles[5]);
    }

    @OnClick({R.id.just_btn, R.id.fromArray_btn, R.id.defer_btn, R.id.timer_btn, R.id.interval_btn})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.just_btn:
                just();
                break;
            case R.id.fromArray_btn:
                fromArray();
                break;
            case R.id.defer_btn:
                defer();
                break;
            case R.id.timer_btn:
                timer();
                break;
            case R.id.interval_btn:
                interval();
                break;
            default:
                break;
        }
    }

    /**
     * 使用Just()快速创建Observable
     */
    private void just() {
        Observable.just(1, 2, 3, 4).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                sb.append(value);
                sb.append(",");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                justBtn.setText(sb);
            }

        });
    }

    /**
     * 使用fromArray()快速创建Observable
     */
    private void fromArray() {
        Observable.fromArray(nums).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                nums_sb.append(value);
                nums_sb.append(",");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                fromArrayBtn.setText(nums_sb);
            }
        });
    }

    /**
     * defer()直到有观察者（Observer ）订阅时，才动态创建被观察者对象（Observable） & 发送事件
     */
    private void defer() {
        Observable<Integer> defer = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i);
            }
        });

        i = 15;
        defer.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                deferBtn.setText("i=" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * timer()发送事件的特点：延迟指定时间后，发送1个数值0（Long类型）
     * 注：timer操作符默认运行在一个新线程上
     */
    private void timer() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(final Long value) {
                Log.d(TAG, "接收到了事件" + value);
                timerBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        timerBtn.setText("接收到:" + value);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }

    /**
     * interval（）发送事件的特点：每隔指定时间 就发送 事件
     * 注：interval默认在computation调度器上执行
     */
    private Disposable disposable;

    private void interval() {
        // 参数说明：
        // 参数1 = 第1次延迟时间；
        // 参数2 = 间隔时间数字；
        // 参数3 = 时间单位；
        Observable.interval(0, 1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(final Long value) {
                Log.d(TAG, "接收到了事件" + value);
                intervalBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        intervalBtn.setText(String.valueOf(value));
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null ) {
            disposable.dispose();
        }
    }
}

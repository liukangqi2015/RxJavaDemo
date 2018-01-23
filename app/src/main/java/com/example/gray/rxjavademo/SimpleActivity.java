package com.example.gray.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SimpleActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView textView;
    private StringBuilder builder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        ButterKnife.bind(this);
        //1.创建被观察者Observable对象
        // create() 是 RxJava 最基本的创造事件序列的方法
        // 此处传入了一个 OnSubscribe 对象参数
        // 当 Observable 被订阅时，OnSubscribe 的 call() 方法会自动被调用，即事件序列就会依照设定依次被触发
        // 即观察者会依次调用对应事件的复写方法从而响应事件
        // 从而实现被观察者调用了观察者的回调方法 & 由被观察者向观察者的事件传递，即观察者模式
        Observable.create(new ObservableOnSubscribe<String>() {
            // 2. 在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                // 通过 ObservableEmitter类对象产生事件并通知观察者
                // ObservableEmitter类介绍
                // a. 定义：事件发射器
                // b. 作用：定义需要发送的事件 & 向观察者发送事件
                e.onNext("Hello");
                e.onNext("World");
                e.onNext("Hello");
                e.onNext("World");
                e.onNext("Hello");
                e.onNext("World");
                e.onComplete();
            }
        }).subscribe(
                //创建观察者 （Observer ）对象
                new Observer<String>() {
                    // 2. 创建对象时通过对应复写对应事件方法 从而 响应对应事件

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.e("onNext", value);
                        builder.append(value);
                        builder.append("\n");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        textView.setText(builder);
                    }
                });
    }
}

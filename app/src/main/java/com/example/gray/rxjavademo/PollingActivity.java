package com.example.gray.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gray.rxjavademo.api.JinshanWord;
import com.example.gray.rxjavademo.bean.Translation;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 使用RxJava实现网络轮询
 */
public class PollingActivity extends AppCompatActivity {
    @BindView(R.id.start_poll_btn)
    Button start_poll_btn;
    @BindView(R.id.content_tv)
    TextView content_tv;
    private JinshanWord jinshanWord;
    private static final String TAG="RxJava";
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);
        ButterKnife.bind(this);
        initRetrofit();
    }


    // URL模板 http://fy.iciba.com/ajax.php
    // URL实例 http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world
    // 参数说明：
    // a：固定值 fy
    // f：原文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
    // t：译文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
    // w：查询内容

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://fy.iciba.com/")
        // 设置 网络请求 Url
        .addConverterFactory(GsonConverterFactory.create())
        //设置使用Gson解析(记得加入依赖)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        // 支持RxJava
        .build();
        jinshanWord = retrofit.create(JinshanWord.class);

    }

    @OnClick(R.id.start_poll_btn)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_poll_btn:
                startPoll();
                break;
        }
    }

    private StringBuilder builder=new StringBuilder();
    private void startPoll() {
        Observable.interval(0,2, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "第 " + aLong + " 次轮询" );
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Long value) {
//                Log.d(TAG, "第 " + value + " 次轮询" );
                jinshanWord.getCall()
                        .subscribeOn(Schedulers.io())//被观察者子线程请求数据
                        .observeOn(AndroidSchedulers.mainThread())//观察者主线程接收数据
                        .subscribe(new Observer<Translation>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Translation result) {
                                result.show();
                                builder.append(result.getContent().getOut());
                                builder.append("\n");
                                content_tv.setText(builder);

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
        if (disposable!=null){
            disposable.dispose();
        }
    }
}

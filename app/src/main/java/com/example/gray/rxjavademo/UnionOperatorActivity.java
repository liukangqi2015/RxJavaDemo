package com.example.gray.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gray on 2018/1/29.
 * RxJava合并操作符页面
 */

public class UnionOperatorActivity extends AppCompatActivity {

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
                break;
            case R.id.merge_btn:
                break;
            case R.id.concatDelayError_btn:
                break;
            case R.id.zip_btn:
                break;
        }
    }
}

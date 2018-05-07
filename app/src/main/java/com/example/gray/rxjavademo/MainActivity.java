package com.example.gray.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

/**
 * Created by Gray on 2017/11/23.
 * 主界面
 */

public class MainActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.simple_activity_btn)
    Button simple_activity_btn;
    @Nullable
    @BindView(R.id.operator_btn)
    Button operator_btn;
    @BindView(R.id.transform_btn)
    Button transformBtn;
    @BindView(R.id.union_btn)
    Button unionBtn;
    @BindView(R.id.polling_btn)
    Button polling_btn;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }


    @Optional
    @OnClick({R.id.simple_activity_btn, R.id.operator_btn, R.id.transform_btn,R.id.union_btn,R.id.polling_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simple_activity_btn:
                startActivity(new Intent(this, SimpleActivity.class));
                break;
            case R.id.operator_btn:
                startActivity(new Intent(this, CreateOperatorActivity.class));
                break;
            case R.id.transform_btn:
                startActivity(new Intent(this, TransformOperatorActivity.class));
                break;
            case R.id.union_btn:
                startActivity(new Intent(this,UnionOperatorActivity.class));
                break;
            case R.id.polling_btn:
                startActivity(new Intent(this,PollingActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

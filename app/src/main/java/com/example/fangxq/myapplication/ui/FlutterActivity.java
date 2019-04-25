package com.example.fangxq.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterView;

/**
 * @author huiguo
 * @date 2019/4/25
 */
public class FlutterActivity extends AppCompatActivity {

    public static final String CHANNEL_NAME = "com.flutterbus/demo";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String routeName = "main";
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            routeName = intent.getExtras().getString("routeName");
        }

        FlutterView flutterView = Flutter.createView(this,this.getLifecycle(), routeName);
        // 创建Platform Channel用来和Flutter层进行交互
        new MethodChannel(flutterView, CHANNEL_NAME).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                methodCall(methodCall, result);
            }
        });
        setContentView(flutterView);
    }

    /**
     * 处理dart层传来的方法调用
     */
    private void methodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("gotoUiTestPage")) {
            startActivity(new Intent(this, UITestActivity.class));
            result.success(true);
        } else {
            result.notImplemented();
        }
    }
}

package com.example.fangxq.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.view.FlutterView;

/**
 * @author huiguo
 * @date 2019/4/25
 */
public class FlutterDemoActivity extends FragmentActivity {

    public static final String CHANNEL_NAME_METHOD = "com.flutterbus/demo/method";
    public static final String CHANNEL_NAME_MESSAGE = "com.flutterbus/demo/message";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String routeStr = "main?action1=aaaa&action2=bbbb";
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            routeStr = intent.getExtras().getString("routeStr?action1=aaaa&action2=bbbb");
        }

        FlutterView flutterView = Flutter.createView(this,this.getLifecycle(), routeStr);
        // 创建Platform Channel用来和Flutter层进行交互
        new MethodChannel(flutterView, CHANNEL_NAME_METHOD).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                methodCall(methodCall, result);
            }
        });

        BasicMessageChannel basicMessageChannel =  new BasicMessageChannel(flutterView, CHANNEL_NAME_MESSAGE, new StandardMessageCodec());
        basicMessageChannel.setMessageHandler(new BasicMessageChannel.MessageHandler() {
            @Override
            public void onMessage(Object o, BasicMessageChannel.Reply reply) {
                Object object = o;
                Log.e("flutter", "android receive message + " + o.toString());
                reply.reply("i am android side");
            }
        });
        setContentView(flutterView);
    }

    /**
     * 处理dart层传来的方法调用
     */
    private void methodCall(MethodCall call, MethodChannel.Result result) {
        Object object = call.argument("object");
        if (call.method.equals("gotoUiTestPage")) {
            startActivity(new Intent(this, UITestActivity.class));
            result.success(true);
        } else {
            result.notImplemented();
        }
    }
}

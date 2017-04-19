package com.dai;

import android.app.Application;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by dai on 2017/4/6.
 */

public class ChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "1cbe3c3ed61d0", "a866979838af5e8633e87cc9542d43ec");
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }
}

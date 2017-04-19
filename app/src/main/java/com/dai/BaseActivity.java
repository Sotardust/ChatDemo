package com.dai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dai.util.ChatWebSocketListener;

import okhttp3.Request;


/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class BaseActivity extends AppCompatActivity {

    ChatWebSocketListener chatWebSocketListener;

    public ChatWebSocketListener getChatWebSocketListener() {
        return chatWebSocketListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatWebSocketListener = new ChatWebSocketListener(this);
    }

    public static Request.Builder getRequest(String url) {
        return new Request.Builder()
                .addHeader("Connection", "close")
                .url(url);
    }
}

package com.dai.util;

import android.app.Activity;

import com.dai.bean.ChatData;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class ChatWebSocketListener extends WebSocketListener {

    private static Activity mActivity;
    private static WebSocket mWebSocket;


    public ChatWebSocketListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        mWebSocket = webSocket;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        System.out.println("onMessage text = " + text);
        ChatDataObserved chatDataObserved = ChatDataObserved.getInstance();
        chatDataObserved.add((ChatDataObserver) mActivity);
        ChatData chatData = new ChatData();
        chatData.setText(text);
        chatDataObserved.notifyText(chatData);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        System.out.println("onClosed code = " + code);
        System.out.println("onClosed reason = " + reason);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        System.out.println("bytes = " + bytes);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        t.printStackTrace();
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        System.out.println("onClosing code = " + code);
        System.out.println("onClosing reason = " + reason);
    }

    public void sendMessage(String text) {
        mWebSocket.send(text);
    }

    public void closeWebSocket() {
        mWebSocket.close(1000, "主动关闭");
    }


}

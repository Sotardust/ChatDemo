package com.dai;

import android.os.Bundle;
import android.widget.ListView;

import com.dai.bean.ChatMessage;
import com.dai.bean.ReceivedMessage2;
import com.dai.login.LoginActivity;
import com.dai.util.ChatDataObserver;
import com.dai.util.MainAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends BaseActivity implements ChatDataObserver {

    private ListView listView;
    private MainAdapter mainAdapter;
    private static ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoginActivity loginActivity = new LoginActivity();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId("original");
        chatMessage.setContent("欢迎进入 original 房间");
        Date date = new Date();
        long time = date.getTime();
        chatMessage.setTimeStamp(time);
        chatMessage.setNumber(1);
        chatMessages.add(chatMessage);
        listView = (ListView) findViewById(R.id.room);
        mainAdapter = new MainAdapter(getApplicationContext(), chatMessages);
        listView.setAdapter(mainAdapter);
    }

    @Override
    public void onTextMessage(String string) {
        System.out.println("MainActivity2.onTextMessage");
        System.out.println("string = " + string);
        Gson gson = new Gson();
        ReceivedMessage2 receivedMessage = gson.fromJson(string, ReceivedMessage2.class);
        for (ChatMessage chatMessage : chatMessages) {
            if (chatMessage.getRoomId().equals(receivedMessage.getRoomId())) {
                return;
            }
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(receivedMessage.getRoomId());
        chatMessage.setContent(receivedMessage.getMessage());
        chatMessage.setTimeStamp(receivedMessage.getTimeStamp());
        chatMessage.setNumber(1);
        chatMessages.add(chatMessage);
        mainAdapter = new MainAdapter(getApplicationContext(), chatMessages);
        listView.setAdapter(mainAdapter);
    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}
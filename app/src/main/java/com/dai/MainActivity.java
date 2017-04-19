package com.dai;

import android.os.Bundle;
import android.widget.ListView;

import com.dai.bean.ChatMessage;
import com.dai.login.LoginActivity;
import com.dai.util.ChatDataObserver;
import com.dai.util.MainAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends BaseActivity implements ChatDataObserver {

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
        System.out.println("MainActivity.onTextMessage");
        System.out.println("string = " + string);
        try {
            JSONObject jsonObject = new JSONObject(string);
            String roomId = jsonObject.getString("roomid");
            String appId = jsonObject.getString("appid");
            String cmd = jsonObject.getString("cmd");
            String msg = jsonObject.getString("msg");
            long time = jsonObject.getLong("time");
            String uid = jsonObject.getString("uid");
            for (ChatMessage chatMessage : chatMessages) {
                if (chatMessage.getRoomId().equals(roomId)) {
                    return;
                }
            }
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setRoomId(roomId);
            chatMessage.setContent(msg);
            chatMessage.setTimeStamp(time);
            chatMessage.setNumber(1);
            chatMessages.add(chatMessage);
            System.out.println("roomId = " + roomId);
            System.out.println("appId = " + appId);
            System.out.println("cmd = " + cmd);
            System.out.println("msg = " + msg);
            System.out.println("time = " + time);
            System.out.println("uid = " + uid);
            mainAdapter = new MainAdapter(getApplicationContext(), chatMessages);
            listView.setAdapter(mainAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}

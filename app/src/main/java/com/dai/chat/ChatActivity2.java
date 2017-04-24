package com.dai.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dai.BaseActivity;
import com.dai.R;
import com.dai.bean.Message;
import com.dai.bean.ReceivedMessage;
import com.dai.bean.SendMessage;
import com.dai.login.RegisterActivity;
import com.dai.util.ChatDataObserver;
import com.dai.util.SimpleTextWatcher;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class ChatActivity2 extends BaseActivity implements ChatDataObserver {

    private ChatMessageAdapter messageAdapter;
    private ListView listView;
    private EditText input;
    private int number = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");
        String content = intent.getStringExtra("content");
        listView = (ListView) findViewById(R.id.chat_list);
        TextView room = (TextView) findViewById(R.id.chat_room_id);
        input = (EditText) findViewById(R.id.chat_input);
        final Button send = (Button) findViewById(R.id.chat_send);

        room.setText(roomId);
        input.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                RegisterActivity.setButtonColor(s.toString(), send);
            }
        });
        ArrayList<Message> messages = new ArrayList<>();
        messageAdapter = new ChatMessageAdapter(getApplicationContext(), messages);
        listView.setAdapter(messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setHeadIcon(R.mipmap.ic_launcher);
                message.setComing(false);
                message.setName("小明");
                Date date = new Date();
                message.setDate(date);
                message.setMessage(input.getText().toString());
                messageAdapter.setMessages(message);

                SendMessage sendMessage = new SendMessage();
                long clientTime = date.getTime();
                sendMessage.setRoomId("6666");
                sendMessage.setUserId("xiaoming");
                sendMessage.setMessage(input.getText().toString());
                sendMessage.setTimeStamp(date.getTime());
                getChatWebSocketListener().sendMessage(sendMessage.toString());
            }
        });
    }

    @Override
    public void onTextMessage(String string) {
        Gson gson = new Gson();
        ReceivedMessage receivedMessage = gson.fromJson(string, ReceivedMessage.class);
        Message message = new Message();
        message.setHeadIcon(R.mipmap.ic_launcher);
        if (number % 2 == 0) {
            message.setComing(false);
        } else {
            message.setComing(true);
        }
        message.setName("小王");
        Date date = new Date();
        message.setDate(date);
        message.setMessage(receivedMessage.getMessage());
        messageAdapter.setMessages(message);
    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}

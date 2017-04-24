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
import com.dai.bean.ReceivedMessage2;
import com.dai.bean.SendMessage2;
import com.dai.login.LoginActivity;
import com.dai.login.RegisterActivity;
import com.dai.util.ChatDataObserver;
import com.dai.util.SimpleTextWatcher;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class ChatActivity2 extends BaseActivity implements ChatDataObserver {

    private ChatMessageAdapter messageAdapter;
    private ListView listView;
    private EditText input;
    private int number = 0;
    private List<Message> messages;

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
        messages = new ArrayList<>();
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
                messages.add(message);
                messageAdapter.setMessages(messages);
                listView.setSelection(messageAdapter.getCount());

                SendMessage2 sendMessage = new SendMessage2();
                sendMessage.setRoomId("6666");
                sendMessage.setUserId(LoginActivity.getUserId());
                sendMessage.setMessage(input.getText().toString());
                sendMessage.setTimeStamp(date.getTime());
                getChatWebSocketListener().sendMessage(sendMessage.toString());
            }
        });
    }

    @Override
    public void onTextMessage(String string) {
        Gson gson = new Gson();
        ReceivedMessage2 receivedMessage = gson.fromJson(string, ReceivedMessage2.class);
        if (LoginActivity.getUserId().equals(receivedMessage.getUserId())) {
            return;
        }
        final Message message = new Message();
        message.setHeadIcon(R.mipmap.ic_launcher);
        message.setComing(true);
        message.setName("小王");
        Date date = new Date();
        message.setDate(date);
        message.setMessage(receivedMessage.getMessage());
        messages.add(message);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.setMessages(messages);
                listView.setSelection(messageAdapter.getCount());
            }
        });
    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}

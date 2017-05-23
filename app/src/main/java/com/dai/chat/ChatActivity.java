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
import com.dai.login.RegisterActivity;
import com.dai.util.ChatDataObserver;
import com.dai.util.SimpleTextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class ChatActivity extends BaseActivity implements ChatDataObserver {

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
//                if (position % 2 != 0) {
                message.setComing(false);
                message.setName("小明");
//                } else {
//                    message.setName("小明" + position % 2);
//                    message.setComing(true);
//                }
                Date date = new Date();
                message.setDate(date);
                message.setMessage(input.getText().toString());
//                messageAdapter.setMessages(message);

                JSONObject jsonObject = new JSONObject();
                try {
                    long clientTime = date.getTime();
                    jsonObject.put("cmd", "room.chat");
                    jsonObject.put("roomid", "original");
                    jsonObject.put("msg", input.getText().toString());
                    jsonObject.put("post", "1");
                    jsonObject.put("ctime", String.valueOf(clientTime));
                    System.out.println("clientTime = " + clientTime);
                    getChatWebSocketListener().sendMessage(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onTextMessage(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String roomId = jsonObject.getString("roomid");
            String appId = jsonObject.getString("appid");
            String cmd = jsonObject.getString("cmd");
            final String msg = jsonObject.getString("msg");
            String time = jsonObject.getString("time");
            String uid = jsonObject.getString("uid");

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
            message.setMessage(msg);
//            messageAdapter.setMessages(message);
            number++;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}

package com.dai.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dai.BaseActivity;
import com.dai.R;
import com.dai.login.RegisterActivity;
import com.dai.util.ChatDataObserver;
import com.dai.util.SimpleTextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class ChatActivity extends BaseActivity implements ChatDataObserver {

    TextView chatContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        String roomId = intent.getStringExtra("roomId");
        String content = intent.getStringExtra("content");
//        ListView listView = (ListView) findViewById(R.id.chat);
        TextView room = (TextView) findViewById(R.id.chat_room_id);
        chatContent = (TextView) findViewById(R.id.chat_content);
        final EditText input = (EditText) findViewById(R.id.chat_input);
        final Button send = (Button) findViewById(R.id.chat_send);

        room.setText(roomId);
        chatContent.setText(content);
        input.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                RegisterActivity.setButtonColor(s.toString(), send);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    Date date = new Date();
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
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatContent.setText(msg);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}

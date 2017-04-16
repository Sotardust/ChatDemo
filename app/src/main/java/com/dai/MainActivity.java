package com.dai;

import android.os.Bundle;
import android.widget.ListView;

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
    private static ArrayList<String> ids = new ArrayList<>();
    private static ArrayList<String> content = new ArrayList<>();
    private static ArrayList<String> times = new ArrayList<>();
    private static ArrayList<Integer> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoginActivity loginActivity = new LoginActivity();

        ids.add("original");
        content.add("欢迎进入 original 房间");
        Date date = new Date();
        long time = date.getTime();
        times.add(String.valueOf(time));
        numbers.add(1);
        listView = (ListView) findViewById(R.id.room);
        mainAdapter = new MainAdapter(getApplicationContext(), ids, content, times, numbers);
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
            String time = jsonObject.getString("time");
            String uid = jsonObject.getString("uid");
            if (ids.contains(roomId)) {
                return;
            }
            ids.add(roomId);
            content.add(msg);
            times.add(time);
            numbers.add(1);
            System.out.println("roomId = " + roomId);
            System.out.println("appId = " + appId);
            System.out.println("cmd = " + cmd);
            System.out.println("msg = " + msg);
            System.out.println("time = " + time);
            System.out.println("uid = " + uid);
            mainAdapter = new MainAdapter(getApplicationContext(), ids, content, times, numbers);
            listView.setAdapter(mainAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}

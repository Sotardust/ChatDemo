package com.dai.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dai.BaseActivity;
import com.dai.MainActivity;
import com.dai.R;
import com.dai.util.ChatDataObserver;
import com.dai.util.SimpleTextWatcher;
import com.dai.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by dai on 2017/4/6.
 */

public class LoginActivity extends BaseActivity implements ChatDataObserver {


    private static WebSocket webSocket;
    private Handler handler;

    public static WebSocketListener getWebSocketListener() {
        return webSocketListener;
    }

    private static WebSocketListener webSocketListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        };

        bindView();
    }

    public static WebSocket getWebSocket() {
        return webSocket;
    }

    private void bindView() {
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final TextView register = (TextView) findViewById(R.id.register);
        TextView forget = (TextView) findViewById(R.id.forget);
        final Button login = (Button) findViewById(R.id.login);
        username.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                setButtonColor(s.toString(), password.getText().toString(), login);
            }
        });
        password.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                setButtonColor(s.toString(), username.getText().toString(), login);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity2.class);
                intent.putExtra("content", register.getText().toString());
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login(username.getText().toString(), password.getText().toString());
//                pullData(username.getText().toString(), password.getText().toString());
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


    }


    private void login(final String username, final String password) {
        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = Url.getLoginUrl();
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .build();
                Request request =BaseActivity.getRequest(url)
                        .post(requestBody)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                System.out.println("response.code() = " + response.code());
                String content = response.body().string();
                System.out.println("content = " + content);
                e.onSuccess(content);


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String value) {
                        System.out.println("value = " + value);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(value);
                            int success = jsonObject.getInt("success");
                            String error = jsonObject.getString("error");
                            if (success == 1) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }

    /**
     * 账号和密码不为空时登录按钮可点击
     *
     * @param s1    String
     * @param s2    String
     * @param login 登录按钮
     */
    private void setButtonColor(String s1, String s2, Button login) {
        if (!s1.isEmpty() && !s2.isEmpty()) {
            login.setClickable(true);
            login.setBackgroundResource(R.drawable.login_text_click_fill);
        } else {
            login.setClickable(false);
            login.setBackgroundResource(R.drawable.login_text_not_click_fill);
        }
    }

    private void pullData(final String username, final String password) {
        String url = "ws://8.34.216.83:8080";
        OkHttpClient okHttpClient = new OkHttpClient();
        Date date = new Date();
        String time = String.valueOf(date.getTime()).substring(0, 10);
        String content = "6666" + username + password;
        String sign = RegisterActivity2.textEncryption(RegisterActivity2.textEncryption(content) + time);
        Request request = new Request.Builder()
                .header("Appid", "6666")
                .header("Uid", username)
                .header("Psw", password)
                .header("Time", time)
                .header("Tag", "phone")
                .header("Sign", sign)
                .url(url)
                .build();

        webSocket = okHttpClient.newWebSocket(request, getChatWebSocketListener());
    }

    @Override
    public void onTextMessage(String text) {
        String loginInfo = "登录失败";
        if (!text.contains("code")) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(text);
            int code = jsonObject.getInt("code");
            String content = jsonObject.getString("content");
            if (code == 2000) {
                loginInfo = "登录成功";
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } finally {
            handler.sendMessage(handler.obtainMessage(0, loginInfo));
        }
    }

    @Override
    public void onByteMessage(Byte abyte) {

    }
}

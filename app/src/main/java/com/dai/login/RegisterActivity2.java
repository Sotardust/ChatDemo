package com.dai.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dai.BaseActivity;
import com.dai.R;
import com.dai.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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


/**
 * Created by Administrator on 2017/4/9 0009.
 */

public class RegisterActivity2 extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        View view = findViewById(R.id.register_bar2);
        ImageView back = (ImageView) view.findViewById(R.id.bar_back);
        TextView title = (TextView) view.findViewById(R.id.bar_content);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("注册账号");

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirm_password);
        Button completed = (Button) findViewById(R.id.completed);

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount(email, password, confirmPassword);
            }
        });
    }


    private void registerAccount(EditText email, final EditText password, final EditText confirmPassword) {
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(confirmPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.getText().toString().isEmpty()) {
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(getApplicationContext(), "两次密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                confirmPassword.setText("");
            }
        }
        postUserInfo(email.getText().toString(), confirmPassword.getText().toString());
    }

    private void postUserInfo(final String username, final String password) {


        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {

                OkHttpClient okhttpClient = new OkHttpClient();
                String url = Url.getRegisterUrl();
                System.out.println("url = " + url);

                Date date = new Date();
                String time = String.valueOf(date.getTime()).substring(0, 10);
                System.out.println("date.getTime() = " + String.valueOf(date.getTime()).substring(0, 10));
                String sign = "{^_^}..(^~^)" + username + password + time + "6666";

                RequestBody requestBody = new FormBody.Builder()
                        .add("appid", "6666")
                        .add("uid", username)
                        .add("psw", password)
                        .add("tag", "18222171946")
                        .add("time", time)
                        .add("sign", textEncryption(sign))
                        .build();

                // 47.93.47.203 注册
//                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("username", username);
//                jsonObject.put("password", password);
//                jsonObject.put("email", "958471598@qq.com");
//                jsonObject.put("number", "18222171946");
//                System.out.println("jsonObject = " + jsonObject);
//                RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Response response = okhttpClient.newCall(request).execute();
                String content = response.body().string();
                emitter.onSuccess(content);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String value) {
                        System.out.println(value);
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            int code = jsonObject.getInt("code");
                            String content = "注册失败";
                            if (code == 2000) {
                                content = "注册成功";
                            }
                            Toast.makeText(getBaseContext(), content, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public static String textEncryption(String text) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(text.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    strBuf.append(Integer.toHexString(0xff & anEncryption));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}

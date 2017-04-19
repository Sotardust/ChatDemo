package com.dai.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dai.BaseActivity;
import com.dai.R;
import com.dai.util.SimpleTextWatcher;

import java.util.Timer;

import cn.smssdk.SMSSDK;

/**
 * Created by dai on 2017/4/7.
 */

public class ForgetPasswordActivity extends BaseActivity {

    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        View view = findViewById(R.id.forget_bar);
        ImageView back = (ImageView) view.findViewById(R.id.bar_back);
        final TextView title = (TextView) view.findViewById(R.id.bar_content);
        title.setText("重置密码");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final LinearLayout resetLinear = (LinearLayout) findViewById(R.id.reset_password_linear);
        final LinearLayout setLinear = (LinearLayout) findViewById(R.id.set_password_linear);
        final View view2 = findViewById(R.id.input_verification_linear);

        final EditText number = (EditText) findViewById(R.id.iphone_number);
        final Button verificationCode = (Button) findViewById(R.id.get_verification_code);

        EditText input = (EditText) view2.findViewById(R.id.input_verification);
        TextView number2 = (TextView) view2.findViewById(R.id.iphone_number2);
        final TextView change = (TextView) view2.findViewById(R.id.change_content);
        final TextView timing = (TextView) view2.findViewById(R.id.timing);
        final TextView resend = (TextView) view2.findViewById(R.id.resend);
        final Button nextStep = (Button) view2.findViewById(R.id.next_step);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if ((int) msg.obj > 0) {
                    timing.setText((int) msg.obj + "秒后可重发");
                } else {
                    timing.setVisibility(View.GONE);
                    resend.setVisibility(View.VISIBLE);
                    timer.cancel();
                }
            }
        };
        number.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                RegisterActivity.setButtonColor(s.toString(), verificationCode);
            }
        });
        verificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLinear.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                title.setText("验证手机号");
                change.setText("验证码已发送到以下手机，请在3分钟内完成重置");
                timer = new Timer();
                RegisterActivity.setTimer(handler, timer, timing, resend);
            }
        });


        input.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                RegisterActivity.setButtonColor(s.toString(), nextStep);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                RegisterActivity.setTimer(handler, timer, timing, resend);
                SMSSDK.getVerificationCode("86", number.getText().toString());
            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view2.setVisibility(View.GONE);
                setLinear.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "下一步", Toast.LENGTH_SHORT).show();
                title.setText("设置密码");
            }
        });

        EditText password = (EditText) findViewById(R.id.password);
        EditText confirmPassword = (EditText) findViewById(R.id.confirm_password);
        Button completed = (Button) findViewById(R.id.completed);
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}

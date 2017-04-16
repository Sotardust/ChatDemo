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
import java.util.TimerTask;

import cn.smssdk.SMSSDK;

/**
 * Created by dai on 2017/4/6.
 */

public class RegisterActivity extends BaseActivity {

    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final boolean[] flag = {true};
        View view = findViewById(R.id.register_bar);
        final View view2 = findViewById(R.id.input_verification_linear);
        ImageView back = (ImageView) view.findViewById(R.id.bar_back);
        final TextView content = (TextView) view.findViewById(R.id.bar_content);
        final LinearLayout registerLinear = (LinearLayout) findViewById(R.id.get_verification_code_linear);
        final EditText number = (EditText) findViewById(R.id.iphone_number);
        final Button verificationCode = (Button) findViewById(R.id.get_verification_code);
        final String contentString = getIntent().getStringExtra("content");
        EditText input = (EditText) view2.findViewById(R.id.input_verification);
        TextView number2 = (TextView) view2.findViewById(R.id.iphone_number2);
        final TextView timing = (TextView) view2.findViewById(R.id.timing);
        final TextView resend = (TextView) view2.findViewById(R.id.resend);
        final Button nextStep = (Button) view2.findViewById(R.id.next_step);
        content.setText(contentString);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0]) {
                    finish();
                } else {
                    flag[0] = true;
                    registerLinear.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    content.setText(contentString);
                    timer.cancel();
                }
            }
        });
        number.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                setButtonColor(s.toString(), verificationCode);
            }
        });
        number2.setText("+86" + number.getText().toString());
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
        verificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.getVerificationCode("86", number.getText().toString());
                timer = new Timer();
                flag[0] = false;
                content.setText("验证手机号");
                registerLinear.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                setTimer(handler, timer, timing, resend);
            }
        });


        input.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                setButtonColor(s.toString(), nextStep);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer(handler, timer, timing, resend);
                SMSSDK.getVerificationCode("86", number.getText().toString());
            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "下一步", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setButtonColor(String string, Button button) {
        System.out.println("string.isEmpty() = " + string.isEmpty());
        if (!string.isEmpty()) {
            button.setBackgroundResource(R.drawable.login_text_click_fill);
            button.setEnabled(true);
        } else {
            button.setBackgroundResource(R.drawable.login_text_not_click_fill);
            button.setEnabled(false);
        }
    }

    public static void setTimer(final Handler handler, Timer timer, TextView timing, TextView resend) {
        timer = new Timer();
        final int[] second = {60};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage(0, --second[0]));
                System.out.println("RegisterActivity.run");
            }
        }, 1, 1000);
        timing.setVisibility(View.VISIBLE);
        resend.setVisibility(View.GONE);
    }


}

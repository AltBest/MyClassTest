package com.feicui.contacts.myclasstest;
import android.widget.TextView;

public class SmsActivity extends BaseActivity {

    TextView tv_persons;
    TextView tv_messages;
    @Override
    protected int setContent() {
        return R.layout.activity_sms;
    }

    @Override
    protected void initView() {
        tv_persons = (TextView) findViewById(R.id.tv_persons);
        tv_messages = (TextView) findViewById(R.id.tv_messages);
        tv_persons.setText("发件人："+ getIntent().getStringExtra("person"));
        tv_messages.setText("内容："+ getIntent().getStringExtra("message"));
    }
    @Override
    protected void setListener() {

    }
}

package com.feicui.contacts.myclasstest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.feicui.contacts.myclasstest.adapter.SmsAdapter;
import com.feicui.contacts.myclasstest.eneity.SmsInfo;

import java.util.ArrayList;

public class SmsContent extends BaseActivity implements AdapterView.OnItemClickListener {
    ListView lv_sms;
    ArrayList<SmsInfo> smsList = new ArrayList<SmsInfo>();
    Cursor mcursor;
    Uri SMS_INBOX = Uri.parse("content://sms/");
    @Override
    protected int setContent() {
        return R.layout.activity_sms_content;
    }

    @Override
    protected void initView() {
        lv_sms = (ListView) findViewById(R.id.lv_sms);
        readSms();
        requestPermission();
    }

    @Override
    protected void setListener() {
        lv_sms.setOnItemClickListener(this);
    }

    private void readSms(){
        //需要查询的信息
        String[] projection = new String[]{
                 "address", "body"
        };
        //查询返回游标
        mcursor = getContentResolver().query(
                SMS_INBOX,
                projection,
                null,
                null,
                null);
        mcursor.moveToFirst();
        if (mcursor!=null){
            do {
                String person = mcursor.getString(mcursor.getColumnIndexOrThrow("address"));
                String message = mcursor.getString(mcursor.getColumnIndexOrThrow("body"));
                SmsInfo info = new SmsInfo();
                info.setPerson(person);
                info.setMessage(message);
                smsList.add(info);
            }while (mcursor.moveToNext());
            SmsAdapter adapter = new SmsAdapter(this, smsList);
            lv_sms.setAdapter(adapter);
        }
    }

    /**
     * @description 动态申请权限
     */
    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    0);
        }
    }

    /**
     * @description 设置下拉列表项的监听
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        intent = new Intent(this, SmsActivity.class);
        intent.putExtra("person",smsList.get(position).getPerson());
        intent.putExtra("message",smsList.get(position).getMessage());
        startActivity(intent);
    }
}

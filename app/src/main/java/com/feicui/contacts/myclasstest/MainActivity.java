package com.feicui.contacts.myclasstest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    private final int WRITE_EXTERNAL_STORAGE = 0;
    Button bt_provider;
    Button bt_sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_provider = (Button) findViewById(R.id.bt_provider);
        bt_sms = (Button) findViewById(R.id.bt_sms);
        bt_provider.setOnClickListener(this);
        bt_sms.setOnClickListener(this);
        requestPermission();
    }
    /**
     * 申请运行时权限
     */
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * @description 申请权限点击选项的处理
     * @param requestCode 申请码
     * @param permissions 申请的权限
     * @param grantResults 申请权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断申请码
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //申请第一个权限成功后
                }else{
                    //申请第一个权限失败后
                    finish();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_provider:
                Intent intent = new Intent(MainActivity.this, ContentProviderActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_sms:
                Intent intentsms = new Intent(MainActivity.this, SmsContent.class);
                startActivity(intentsms);
                break;
        }
    }
}

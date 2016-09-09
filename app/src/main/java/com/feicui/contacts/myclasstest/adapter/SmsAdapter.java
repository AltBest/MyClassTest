package com.feicui.contacts.myclasstest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feicui.contacts.myclasstest.R;
import com.feicui.contacts.myclasstest.eneity.SmsInfo;

import java.util.ArrayList;

/**
 * @description 适配器
 * Created by Administrator on 2016/9/9 0009.
 */
public class SmsAdapter extends BaseAdapter {
    private LayoutInflater layoutinflater;
    private ArrayList<SmsInfo> infos;
    public SmsAdapter(Context c, ArrayList<SmsInfo> infos) {
        layoutinflater = LayoutInflater.from(c);
        this.infos = infos;
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutinflater.inflate(R.layout.sms_item, null);
        }
        TextView person = (TextView) convertView.findViewById(R.id.tv_person);
        TextView message = (TextView) convertView.findViewById(R.id.tv_message);
        person.setText("发件人：" + infos.get(position).getPerson());
        message.setText("内容：" + infos.get(position).getMessage());
            return convertView;
    }
}

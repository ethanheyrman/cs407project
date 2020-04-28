package com.example.cs407project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoAdapter extends BaseAdapter {
    Context context;
    String[] PPE;
    LayoutInflater layoutInflater;
    String[] entries;


    public InfoAdapter(Context context, String[] ppe, String[] entries) {
        this.context = context;
        this.PPE = ppe;
        this.entries = entries;
    }

    @Override
    public int getCount() {
        return PPE.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.info_item, parent, false);
            TextView textView = convertView.findViewById(R.id.text);
            textView.setText(PPE[position]);
            TextView textView2 = convertView.findViewById(R.id.text2);
            textView2.setText(entries[position]);
        }
        return convertView;
    }
}
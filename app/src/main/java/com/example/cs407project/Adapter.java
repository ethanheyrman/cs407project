package com.example.cs407project;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;


public class Adapter extends BaseAdapter {

    Context context;
    String[] PPE;
    LayoutInflater layoutInflater;
    String[] editTextEntries;


    public Adapter(Context context, String[] ppe) {
        this.context = context;
        this.PPE = ppe;
        this.editTextEntries = new String[ppe.length];
    }

    @Override
    public int getCount() {
        return PPE.length;
    }

    @Override
    public Object getItem(int position) {
        return editTextEntries[position];
    }

    public String[] getAllEntries()
    {
        return editTextEntries;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_items, parent, false);
            TextView textView = convertView.findViewById(R.id.text);
            textView.setText(PPE[position]);

            EditText editText = convertView.findViewById(R.id.input);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    editTextEntries[position] = s.toString();
                }
            });
            /*
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        Log.i("sdfsdf", Integer.toString(position));
                    }
                }
            }); */

        }
        return convertView;
    }
}


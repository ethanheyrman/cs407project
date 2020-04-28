package com.example.cs407project;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public class ListSentFragment extends Fragment {

    final String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
            "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};
    int type;
    String[] entries;
    SharedPreferences sharedPreferences;

    public ListSentFragment() {
    }

    @SuppressLint("ValidFragment")
    public ListSentFragment(int type, String[] entries, SharedPreferences sharedPreferences) {
        this.type = type;
        this.entries = entries;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_sent, container, false);

        GridView gridView = view.findViewById(R.id.info);
        InfoAdapter adapter = new InfoAdapter(getActivity(), PPETypes, entries);
        gridView.setAdapter(adapter);

        Button button = view.findViewById(R.id.editButton);
        Button button2 = view.findViewById(R.id.deleteButton);
        TextView textView = view.findViewById(R.id.title);
        if (type == 0) {
            textView.setText("I'm Offering: ");
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            offer(v);
                        }
                    }
            );
            button2.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(0);
                        }
                    }
            );
        } else {
            textView.setText("I Need: ");
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            request(v);
                        }
                    }
            );
            button2.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(1);
                        }
                    }
            );
        }
        return view;
    }

    public void request(View view) {
        Intent intent = new Intent(getActivity(), SendPage.class);
        intent.putExtra("type", "request");
        startActivity(intent);
    }

    public void offer(View view) {
        Intent intent = new Intent(getActivity(), SendPage.class);
        intent.putExtra("type", "offer");
        startActivity(intent);
    }

    public void delete(int type) {
        if (type == 0) {
            sharedPreferences.edit().remove("offer").commit();
        } else {
            sharedPreferences.edit().remove("request").commit();
        }
        getActivity().recreate();
    }
}

package com.example.cs407project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.Button;

public class ButtonFragment extends Fragment {

    private int type;

    public ButtonFragment() {
    }

    @SuppressLint("ValidFragment")
    public ButtonFragment(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.button_fragment, container, false);
        Button button = view.findViewById(R.id.button);
        if (type == 0) {
            button.setText("Offer PPE");
            button.setBackgroundColor(Color.parseColor("#ff99cc00"));
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            offer(v);
                        }
                    }
            );
        } else {
            button.setText("Request PPE");
            button.setBackgroundColor(Color.parseColor("#ff669900"));
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            request(v);
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
}

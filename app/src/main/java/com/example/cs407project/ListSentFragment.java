package com.example.cs407project;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


@SuppressLint("ValidFragment")
public class ListSentFragment extends Fragment {

    final String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
            "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};
    int type;
    String[] entries;
    SharedPreferences sharedPreferences;
    DatabaseReference postsBase;
    DatabaseReference geoLoc;
    String username;

    public ListSentFragment() {
    }

    @SuppressLint("ValidFragment")
    public ListSentFragment(int type, String[] entries, SharedPreferences sharedPreferences) {
        this.type = type;
        this.entries = entries;
        this.sharedPreferences = sharedPreferences;
        username = sharedPreferences.getString("username", "");
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
            postsBase = FirebaseDatabase.getInstance().getReference("posts").child(username + "offer");
            geoLoc = FirebaseDatabase.getInstance().getReference("geofireoffers").child(username + "offer");
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
                            confirmDelete(0);
                        }
                    }
            );
        } else {
            textView.setText("I Need: ");
            postsBase = FirebaseDatabase.getInstance().getReference("posts").child(username + "request");
            geoLoc = FirebaseDatabase.getInstance().getReference("geofirerequests").child(username + "request");
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
                            confirmDelete(1);
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

    public void confirmDelete(int type) {

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete post?")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == -1) {
                            actualDelete(type);
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void actualDelete(int type) {
        if (type == 0) {
            sharedPreferences.edit().remove("offer").commit();
            postsBase.removeValue();
            geoLoc.removeValue();
        } else {
            sharedPreferences.edit().remove("request").commit();
            postsBase.removeValue();
            geoLoc.removeValue();
        }
        getActivity().recreate();
    }
}

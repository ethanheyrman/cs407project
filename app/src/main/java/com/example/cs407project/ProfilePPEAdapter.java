package com.example.cs407project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cs407project.models.ProfilePPEPost;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfilePPEAdapter extends ArrayAdapter<ProfilePPEPost> {
    final String[] PPETypes = {"Cloth mask", "Surgical Mask", "Disposable Respirator", "Half Mask",
            "Full Mask", "Mask Filters", "Goggles", "Face Shield", "Surgical Gown"};

    public ProfilePPEAdapter(Context context, ArrayList<ProfilePPEPost> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProfilePPEPost post = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }
        // Lookup view for data population
        TextView type = (TextView) convertView.findViewById(R.id.type);
        TextView clothMaskCount = (TextView) convertView.findViewById(R.id.cloth_masks);
        TextView surgicalMaskCount = (TextView) convertView.findViewById(R.id.surgical_masks);
        TextView respiratorCount = (TextView) convertView.findViewById(R.id.respirators);
        TextView halfMaskCount = (TextView) convertView.findViewById(R.id.half_masks);
        TextView fullMaskCount = (TextView) convertView.findViewById(R.id.full_masks);
        TextView filterCount = (TextView) convertView.findViewById(R.id.filters);
        TextView gogglesCount = (TextView) convertView.findViewById(R.id.goggles);
        TextView faceShieldCount = (TextView) convertView.findViewById(R.id.face_shields);
        TextView gownCount = (TextView) convertView.findViewById(R.id.gowns);

        ArrayList<TextView> textViews = new ArrayList<>(Arrays.asList(clothMaskCount,
                surgicalMaskCount, respiratorCount, halfMaskCount, fullMaskCount, filterCount,
                gogglesCount, faceShieldCount, gownCount));


        type.setText("Type: " + post.type);
        for(int i=0; i<PPETypes.length; i++) {
            if(Integer.parseInt(post.ppe[i]) > 0) {
                textViews.get(i).setText(
                        (PPETypes[i] + ": " + post.ppe[i])
                );
            }
        }
        // Populate the data into the template view using the data object

//        maskCount.setText("Masks: " + post.maskCount);
        // Return the completed view to render on screen
        return convertView;
    }
}

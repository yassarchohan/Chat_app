package com.example.yassarchohan.messages;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yassar chohan on 2/7/2017.
 */
public class Custom_adapter extends ArrayAdapter<gettermethods> {
    public Custom_adapter(Context context, int resource, List<gettermethods> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_custom__view, parent, false);
        }

        TextView txt = (TextView) convertView.findViewById(R.id.todisplayname);
        TextView txt2 = (TextView) convertView.findViewById(R.id.todisplayemail);


        gettermethods message = getItem(position);

        txt.setText(message.getMessage());
        txt2.setText(message.getPhotourl());


        return convertView;
    }
}

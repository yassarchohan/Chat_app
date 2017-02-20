package com.example.yassarchohan.messages;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yassar chohan on 2/10/2017.
 */
public class Custom_adapter2 extends ArrayAdapter<gettermethods> {

    TextView txt,txt2;
    CheckBox chk;

    public Custom_adapter2(Context context, int resource, List<gettermethods> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_custom_view2, parent, false);

            txt = (TextView) convertView.findViewById(R.id.todisplaynameingroup);
            txt2 = (TextView) convertView.findViewById(R.id.todisplayemailingroup);


        }

        gettermethods message = getItem(position);

        txt.setText(message.getMessage());
        txt2.setText(message.getPhotourl());

        return convertView;
    }

}

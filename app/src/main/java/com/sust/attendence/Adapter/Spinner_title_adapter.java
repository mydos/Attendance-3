package com.sust.attendence.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ikhtiar on 5/24/2015.
 */

public class Spinner_title_adapter extends ArrayAdapter<String> {
    private List<String> title;
    private Context context;

    public Spinner_title_adapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.title=objects;
        this.context=context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    public View getCustomView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.spinner_row,parent,false);
        ToastMessage.show_toast(context,"Position : "+position);
        TextView title_tv =(TextView) row.findViewById(R.id.spinner_title_tv);
        title_tv.setText(title.get(position));

//        Button btn =(Button) row.findViewById(R.id.title_cross_button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastMessage.show_toast(context,"Position : "+position);
//            }
//        });

        return row;
    }
}

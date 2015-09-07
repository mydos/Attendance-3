package com.sust.attendence.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sust.attendence.Database.DatabaseWork;

import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ikhtiar on 5/24/2015.
 */

public class Spinner_title_adapter extends ArrayAdapter<String>{
    private List<String> title;
    private Context context;

    public Spinner_title_adapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.title=objects;
        this.context=context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        TextView label;
        Button btn;
        final int pos = position;

        if(convertView==null || !convertView.getTag().toString().equals("DROPDOWN")) {
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
            convertView.setTag("DROPDOWN");

        }
        label = (TextView) convertView.findViewById(R.id.spinner_title_tv);
        label.setText(title.get(position));

        btn =(Button)convertView.findViewById(R.id.title_cross_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                delete_dialog(pos);
            }
        });

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        TextView label;
        Button btn;

        if (convertView == null || !convertView.getTag().toString().equals("NON_DROPDOWN")) {
            convertView = LayoutInflater.from(context).inflate(R.layout.
                    spinner_row_title, parent, false);
            convertView.setTag("NON_DROPDOWN");
        }
        label = (TextView) convertView.findViewById(R.id.spinner_title_tv);
        label.setText(title.get(position));




        return convertView;

     }
    public View getCustomView(final int position, View row, ViewGroup parent){
        TextView label;
        Button btn;
        LayoutInflater inflater;
        if(row==null || !row.getTag().toString().equals("DROPDOWN")) {
                inflater = LayoutInflater.from(context);
                row = inflater.inflate(R.layout.spinner_row, parent, false);
                row.setTag("DROPDOWN");

        }

        label = (TextView) row.findViewById(R.id.spinner_title_tv);
        label.setText(title.get(position));


        btn =(Button)row.findViewById(R.id.title_cross_button);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete_dialog(position);
            }
        });

        return row;
    }
    protected void delete_dialog(int position){
        final int  pos= position;
        final String title_name=title.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("DELETE TITLE "+title.get(pos)+"")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(new DatabaseWork(context).delete_title(title_name)){
                            title.remove(pos);
                            notifyDataSetChanged();
                            ToastMessage.toast_text=title_name+" Deleted.";
                        }else{
                            ToastMessage.toast_text="Encountered en error.";
                        }
                        ToastMessage.show_toast(context,ToastMessage.toast_text);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }
}

package com.sust.attendence.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Manage.ManageActivity;
import com.sust.attendence.Others.Individual_info;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;

import java.util.List;

/**
 * Created by Ikhtiar on 6/10/2015.
 */

public class Listview_individual_adapter extends ArrayAdapter<Individual_info> {
    private Context context;
    private List<Individual_info> individual_info;
    private String title_name;


    public Listview_individual_adapter(Context context, int resource, List objects,String title_name) {
        super(context, resource, objects);
        this.individual_info=objects;
        this.context=context;
        this.title_name=title_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    public View getCustomView(final int position, View row, ViewGroup parent){
        if(row==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.student_list_view, parent, false);
        }

        TextView label1 = (TextView) row.findViewById(R.id.display_reg);
        label1.setText(individual_info.get(position).getReg_no());

        TextView label2 = (TextView) row.findViewById(R.id.display_name);
        label2.setText(individual_info.get(position).getName());

        Button btn =(Button)row.findViewById(R.id.individual_cross_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete_individual(position);
            }
        });


//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (ManageActivity.poss[position] == false)
//                {
//                    v.setBackgroundColor(Color.BLUE);
//                    ManageActivity.poss[position] = true;
//            }
//                else{
//                    v.setBackgroundColor(Color.TRANSPARENT);
//                    ManageActivity.poss[position] = false;
//                }
//                ToastMessage.show_toast(context,"this "+position);
//
//            }
//        });

        if(ManageActivity.pos!=null) {
            if (ManageActivity.pos[position] == true)
                row.setBackgroundColor(Color.RED);
            else {
                row.setBackgroundColor(Color.TRANSPARENT);

            }
        }
//        row.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
////                Intent i = new Intent("");
////                Intent manage_intent = new Intent(this, ManageActivity.class);
////                startActivity(manage_intent);
//                ToastMessage.show_toast(context,"this "+position);
//                return true;
//            }
//        });
        return row;
    }

    protected void delete_individual(int position){
        final int  pos= position;
        final String name = individual_info.get(pos).getName();
        final String reg_no = individual_info.get(pos).getReg_no();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("DELETE INDIVIDUAL "+name+"")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(new DatabaseWork(context).delete_individual(reg_no,title_name)){
                            individual_info.remove(pos);
                            notifyDataSetChanged();
                            ToastMessage.toast_text=name+" Deleted.";
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

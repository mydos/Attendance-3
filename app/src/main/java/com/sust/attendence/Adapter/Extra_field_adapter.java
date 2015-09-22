package com.sust.attendence.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Manage.StudentInformationActivity;
import com.sust.attendence.Others.Absent_Record;
import com.sust.attendence.Others.Extra_Field;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.Others.Utility;
import com.sust.attendence.R;

import java.util.List;

/**
 * Created by Ikhtiar on 9/23/2015.
 */
public class Extra_field_adapter extends ArrayAdapter<Extra_Field> {
    private Context context;
    private List<Extra_Field> extra_fields;



    public Extra_field_adapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.extra_fields = objects;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View row, ViewGroup parent) {
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.extra_field_list_item, parent, false);
        }

        EditText label1 = (EditText) row.findViewById(R.id.display_name_edt);
        label1.setText(extra_fields.get(position).getField_name()+"");

        final EditText label2 = (EditText) row.findViewById(R.id.display_value_edt);
        label2.setText(extra_fields.get(position).getField_value());
//        label2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                label2.setCursorVisible(true);
//            }
//        });
//        label2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                        actionId == EditorInfo.IME_ACTION_DONE ||
//                        event.getAction() == KeyEvent.ACTION_DOWN &&
//                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    if (!event.isShiftPressed()) {
//                        // the user is done typing.
//                        label2.setCursorVisible(false);
//                        Utility.hideSoftKeyboard((Activity) context);
//                        label2.getText();
//                        new DatabaseWork(context).update_coments(absent_record.get(position).getDb_id(),label2.getText().toString());
//                        ToastMessage.show_toast(context, "Done Editing");
//                        return true; // consume.
//                    }
//                }
//                return false;
//            }
//        });

        return row;
    }
//
//    public void make_present(int position){
//        final int  pos= position;
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("SET AS PRESENT")
//                .setPositiveButton("SET", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        if (new DatabaseWork(context).set_as_present(absent_record.get(pos).getDb_id())) {
//                            absent_record.remove(pos);
//                            notifyDataSetChanged();
//                            if(context instanceof StudentInformationActivity)
//                                ((StudentInformationActivity)context).update_present_absent(absent_record.size());
//                            ToastMessage.toast_text = "Set Present.";
//                        } else {
//                            ToastMessage.toast_text = "Encountered en error.";
//                        }
//                        ToastMessage.show_toast(context, ToastMessage.toast_text);
//                    }
//                })
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
//        builder.create();
//        builder.show();
//    }

}

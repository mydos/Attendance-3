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

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Ikhtiar on 9/23/2015.
 */
public class Extra_field_adapter extends ArrayAdapter<Extra_Field>{
    private Context context;
    private List<Extra_Field> extra_fields;
    private String title_name;

    public Extra_field_adapter(Context context, int resource, List objects,String title_name) {
        super(context, resource, objects);
        this.extra_fields = objects;
        this.context = context;
        this.title_name = title_name;
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

        TextView label1 = (TextView) row.findViewById(R.id.display_name_edt);
        label1.setText(extra_fields.get(position).getField_name()+"");

        final EditText label2 = (EditText) row.findViewById(R.id.display_value_edt);
        label2.setText(extra_fields.get(position).getField_value());

        label2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label2.setCursorVisible(true);
            }
        });


        label2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!event.isShiftPressed()) {
                        // the user is done typing.
                        label2.setCursorVisible(false);
                        Utility.hideSoftKeyboard((Activity) context);
                        new DatabaseWork(context).update_extra_field_value(extra_fields.get(position).getField_id(), label2.getText().toString());
                        ToastMessage.show_toast(context, "Done Editing");
                        return true; // consume.
                    }
                }
                return false;
            }
        });

        Button btn = (Button) row.findViewById(R.id.delete_field_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_field(position);
            }
        });

        return row;
    }


    public void delete_field(int position){
        final int  pos= position;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("DELETE THIS FIELD")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (new DatabaseWork(context).manage_field_for_all(title_name,extra_fields.get(pos).getField_name(),"delete_field")>0) {
                            extra_fields.remove(pos);
                            notifyDataSetChanged();
                            ToastMessage.toast_text = "Field Deleted.";
                        } else {
                            ToastMessage.toast_text = "Encountered en error.";
                        }
                        ToastMessage.show_toast(context, ToastMessage.toast_text);
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

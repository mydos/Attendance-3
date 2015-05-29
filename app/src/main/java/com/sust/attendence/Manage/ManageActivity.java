package com.sust.attendence.Manage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sust.attendence.Adapter.Spinner_title_adapter;
import com.sust.attendence.Database.Contract;
import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageActivity extends Activity implements View.OnClickListener {
    private Spinner title_spinner;
    private MyAdapter spinner_adapter_custom;
    private List<String> spinner_item;
    private Button create_title_btn, add_individual_btn;
    private ToggleButton toggle_button;
    private EditText dialog_et_title, dialog_et_ind_reg_no, dialog_et_ind_name;
    private TextView dialog_et_ind_inst_name, dialog_et_ind_title_name;
    private String dialog_et_title_text, dialog_et_ind_name_text, spinner_selected_item, dialog_et_ind_reg_no_text;
    private int dialog_et_ind_reg_number;
    private UserSessionManager session;
    private ListView student_list;
    private SimpleCursorAdapter adapter;
    private ArrayAdapter<String> spinner_adapter;
    private boolean pos[];
    private int total=0;
    String[] objects = {"asd", "fgh", "jkl"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        initialize();

    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.spinner_title_tv);
            label.setText(objects[position]);

            return row;
        }

    }

    protected void initialize() {
        session = new UserSessionManager(this);
        if (session.checkLogin())
            finish();

        title_spinner = (Spinner) findViewById(R.id.title_spinner);
        spinner_item = new ArrayList<String>();
        spinner_item = new DatabaseWork(this).get_title();

        // spinner_adapter = new MyAdapter(this, R.layout.spinner_row, objects);
        spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinner_item);

        title_spinner.setAdapter(spinner_adapter);

        create_title_btn = (Button) findViewById(R.id.create_title_btn);
        add_individual_btn = (Button) findViewById(R.id.add_individual_btn);

        toggle_button = (ToggleButton)findViewById(R.id.toggleButton);
        toggle_button.setOnClickListener(this);

        create_title_btn.setOnClickListener(this);
        add_individual_btn.setOnClickListener(this);

        title_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=null;
                show_student_list();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void show_student_list() {
        student_list = (ListView) findViewById(R.id.individual_list);

        String[] columns = {Contract.Entry_students.STUDENT_COLUMN_NAME_1, Contract.Entry_students.STUDENT_COLUMN_NAME_4};
        int[] views = {R.id.display_reg, R.id.display_name};
        Cursor c = null;

        if (title_spinner.getCount() > 0) {
            spinner_selected_item = title_spinner.getSelectedItem().toString();
            c = new DatabaseWork(this).get_student_list(spinner_selected_item);
            adapter = new SimpleCursorAdapter(this, R.layout.student_list_view, c, columns, views, 0);

        } else {
            adapter = null;
        }
        student_list.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_title_btn:
                appear_title_dialog();
                break;
            case R.id.add_individual_btn:

                if (title_spinner.getCount() <= 0) {
                    ToastMessage.toast_text = "You have not created any title yet ! ";
                    ToastMessage.show_toast(ManageActivity.this, ToastMessage.toast_text);

                } else {
                    spinner_selected_item = title_spinner.getSelectedItem().toString();
                    appear_add_individual_Dialog();

                }
                break;
            case R.id.toggleButton:
                    if(toggle_button.isChecked()){
                        total=0;
                        pos=new boolean[adapter.getCount()];
                        for(int i=0;i<adapter.getCount();i++){
                            pos[i]=false;
                        }
                        student_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(pos[position]) {
                                    view.setBackgroundColor(Color.WHITE);
                                    pos[position]=false;
                                    --total;
                                }else{
                                    view.setBackgroundColor(Color.RED);
                                    pos[position]=true;
                                    ++total;
                                }
                                ToastMessage.show_toast(ManageActivity.this, adapter.getCount()+"  done  "+total);
                            }
                        });
                    }
                else{
                        student_list.setOnItemClickListener(null);
                        show_student_list();
                    }
                break;
        }
    }

    protected void appear_add_individual_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_dialog_add_individual, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog
        // layout
        builder.setView(layout);
        // Add action buttons
        dialog_et_ind_reg_no = (EditText) layout.findViewById(R.id.individual_registration_no_et);
        dialog_et_ind_name = (EditText) layout.findViewById(R.id.individual_name_et);
        dialog_et_ind_inst_name = (TextView) layout.findViewById(R.id.inst_name_tv);
        dialog_et_ind_title_name = (TextView) layout.findViewById(R.id.course_title_tv);

        dialog_et_ind_inst_name.setText("INSTRUCTOR NAME : " + session.get_name());
        dialog_et_ind_title_name.setText("TITLE NAME : " + spinner_selected_item);

        builder.setPositiveButton("SUBMIT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        dialog_et_ind_reg_no_text = dialog_et_ind_reg_no.getText().toString().trim();
                        dialog_et_ind_name_text = dialog_et_ind_name.getText().toString().trim();
                        if (!dialog_et_ind_reg_no_text.equals("") && !dialog_et_ind_name_text.equals("")) {
                            ToastMessage.toast_text = "Individual added Successfully!!!";
                            dialog_et_ind_reg_number = Integer.parseInt(dialog_et_ind_reg_no_text);
                            new DatabaseWork(ManageActivity.this).add_individual(dialog_et_ind_reg_number, dialog_et_ind_name_text, title_spinner.getSelectedItem().toString());
                            show_student_list();
                        } else {
                            ToastMessage.toast_text = "Please Provide required field.";
                        }
                        ToastMessage.show_toast(ManageActivity.this, ToastMessage.toast_text);
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create();
        builder.show();

    }

    protected void appear_title_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_dialog_create_title, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog
        // layout
        builder.setView(layout);
        // Add action buttons
        dialog_et_title = (EditText) layout.findViewById(R.id.create_title_dialog_et);

        builder.setPositiveButton("SUBMIT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        dialog_et_title_text = dialog_et_title.getText().toString().trim();
                        if (!dialog_et_title_text.equals("")) {
                            ToastMessage.toast_text = "Title Created Successfully!!!";
                            if (new DatabaseWork(ManageActivity.this).insert_title(dialog_et_title_text)) {
                                spinner_item.add(dialog_et_title_text);
                                spinner_adapter.notifyDataSetChanged();
                            }

                        } else {
                            ToastMessage.toast_text = "Please Provide Title.";
                        }
                        ToastMessage.show_toast(ManageActivity.this, ToastMessage.toast_text);
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create();
        builder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the others_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.others_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.logout:
                session.logoutUser();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


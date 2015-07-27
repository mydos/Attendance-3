package com.sust.attendence.Manage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.support.v4.app.DialogFragment;

import com.sust.attendence.Adapter.Listview_individual_adapter;
import com.sust.attendence.Adapter.Spinner_title_adapter;
import com.sust.attendence.Call.CallActivity;
import com.sust.attendence.Database.Contract;
import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Listener.DialogListener;
import com.sust.attendence.Others.Individual_info;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ManageActivity extends FragmentActivity implements View.OnClickListener, DialogListener {
    private Spinner title_spinner;
    private Spinner_title_adapter spinner_adapter_custom;
    private Listview_individual_adapter listview_adapter_custom;
    private List<String> spinner_item;
    private Button create_title_btn, add_individual_btn,save_btn;
    private ToggleButton toggle_button;
    private EditText dialog_et_title, dialog_et_ind_reg_no, dialog_et_ind_name;
    private TextView dialog_et_ind_inst_name, dialog_et_ind_title_name;
    private String dialog_et_title_text, dialog_et_ind_name_text, spinner_selected_item, dialog_et_ind_reg_no_text;
    private int dialog_et_ind_reg_number;
    private UserSessionManager session;
    private ListView student_list;
    private SimpleCursorAdapter adapter;
    private ArrayAdapter<String> spinner_adapter;
    public static boolean pos[];
    private int total;
    private DialogFragment df;
    String[] objects = {"asd", "fgh", "jkl"};
    Bundle bdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        check_session();
        initialize();

    }

    protected void check_session() {
        session = new UserSessionManager(this);
        if (session.checkLogin())
            finish();
    }

    protected void initialize() {

        bdl = new Bundle();
        df = new CreateDialog();

        listview_adapter_custom=null;
        pos = null;
        total = 0;
        student_list = null;

        title_spinner = (Spinner) findViewById(R.id.title_spinner);
        create_title_btn = (Button) findViewById(R.id.create_title_btn);
        add_individual_btn = (Button) findViewById(R.id.add_individual_btn);
        toggle_button = (ToggleButton) findViewById(R.id.toggleButton);
        save_btn = (Button) findViewById(R.id.save_btn);

        spinner_item = new ArrayList<String>();
        spinner_item = new DatabaseWork(this).get_title();

        spinner_adapter_custom = new Spinner_title_adapter(this, R.layout.spinner_row, spinner_item);

        title_spinner.setAdapter(spinner_adapter_custom);


        toggle_button.setOnClickListener(this);
        create_title_btn.setOnClickListener(this);
        add_individual_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);

        title_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toggle_button.setChecked(false);
                manage_listitem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void show_student_list() {
        student_list = (ListView) findViewById(R.id.individual_list);

        if (title_spinner.getCount() > 0) {
            spinner_selected_item = title_spinner.getSelectedItem().toString();
            ArrayList<Individual_info> studentsList = new DatabaseWork(this).get_student_list(spinner_selected_item);

            Collections.sort(studentsList,Individual_info.Comparator);

            listview_adapter_custom = new Listview_individual_adapter(this,R.layout.student_list_view,studentsList,spinner_selected_item);
        }
        student_list.setAdapter(listview_adapter_custom);

        student_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent call_intent = new Intent(ManageActivity.this, StudentInformationActivity.class);
                Bundle bdl = new Bundle();
                bdl.putString("title_name",spinner_selected_item);
                call_intent.putExtras(bdl);
                startActivity(call_intent);
                ToastMessage.show_toast(ManageActivity.this,"Yes");
                return true;
            }
        });

    }

    protected void set_listitem_position() {
        if (listview_adapter_custom != null) {
            total = 0;
            pos = new boolean[listview_adapter_custom.getCount()];
            for (int i = 0; i < listview_adapter_custom.getCount(); i++) {
                pos[i] = false;
            }
        }
    }

    protected void manage_listitem() {
        if (toggle_button.isChecked() && listview_adapter_custom != null) {
            save_btn.setVisibility(View.VISIBLE);
            set_listitem_position();
            student_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (pos[position]) {
                        view.setBackgroundColor(Color.TRANSPARENT);
                        pos[position] = false;
                        --total;
                    } else {
                        view.setBackgroundColor(Color.RED);
                        pos[position] = true;
                        ++total;
                    }
                    ToastMessage.show_toast(ManageActivity.this, listview_adapter_custom.getCount() + "  done  " + total);
                }
            });
        } else {
            if (student_list != null)
                student_list.setOnItemClickListener(null);
                pos=null;
            save_btn.setVisibility(View.INVISIBLE);
        }
        show_student_list();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_title_btn:
                bdl.clear();
                bdl.putString("dialog_name", "create_title");
                df.setArguments(bdl);
                df.show(getSupportFragmentManager(), "dialog");

                break;
            case R.id.add_individual_btn:

                if (title_spinner.getCount() <= 0 || toggle_button.isChecked()) {

                    ToastMessage.toast_text = "You have not created any title yet ! ";
                    if(toggle_button.isChecked()){
                        ToastMessage.toast_text = "You have to turn off calling state in order to add ! ";
                    }
                    ToastMessage.show_toast(ManageActivity.this, ToastMessage.toast_text);

                } else {
                    spinner_selected_item = title_spinner.getSelectedItem().toString();

                    bdl.clear();
                    bdl.putString("dialog_name", "add_individual");
                    bdl.putString("spinner_selected_item", spinner_selected_item);
                    df.setArguments(bdl);
                    df.show(getSupportFragmentManager(), "dialog");
                }
                break;
            case R.id.toggleButton:
                   manage_listitem();
                break;

            case R.id.save_btn:
//                new DatabaseWork(this).TEST_af(spinner_selected_item,session.get_inst_id());
//                new DatabaseWork(this).TEST_ar();
//                ToastMessage.show_toast(ManageActivity.this,ToastMessage.toast_text);

               // ToastMessage.show_toast(ManageActivity.this,new DatabaseWork(this).show_test_af());
                //ToastMessage.show_toast(ManageActivity.this,new DatabaseWork(this).show_test_ar());

                bdl.clear();
                bdl.putString("dialog_name","save_roll_call");
                bdl.putInt("total_individual",listview_adapter_custom.getCount());
                bdl.putInt("absent_individual",total);

                df.setArguments(bdl);
                df.show(getSupportFragmentManager(),"dialog");


                break;

        }
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

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog, Bundle bdll) {
        String str = bdll.getString("dialog_name");
        if (str != null) {
            switch (str) {
                case "create_title":
                    if (bdll.getString("dialog_et_title_text") != null)
                        spinner_item.add(bdll.getString("dialog_et_title_text"));
                    spinner_adapter_custom.notifyDataSetChanged();
                    break;
                case "add_individual":
                       show_student_list();
                        break;
                case "save_roll_call":
                    toggle_button.setChecked(false);
                    new DatabaseWork(this).insert_attendence_frequency(spinner_selected_item);
                    ToastMessage.show_toast(this,ToastMessage.toast_text);
                    manage_listitem();
                    break;
                default:
                    break;
            }
        }
        bdll.clear();
        bdl.clear();
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog, Bundle bdll) {
        bdll.clear();
        bdl.clear();
    }
}

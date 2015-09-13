package com.sust.attendence.Others;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sust.attendence.Database.DatabaseHelper;
import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Listener.DialogListener;
import com.sust.attendence.Listener.addCustomTextChangedListener;
import com.sust.attendence.Manage.CreateDialog;
import com.sust.attendence.Manage.ManageActivity;
import com.sust.attendence.R;

public class LogginActivity extends FragmentActivity implements OnClickListener,DialogListener {

    private EditText login_email_et, login_password_et, register_name_et,
            register_email_et, register_password_et;
    private Button login_btn, register_btn;
    private String login_email_et_text, login_password_et_text, register_name_et_text,
            register_email_et_text, register_password_et_text;
    private Context context;
    private SQLiteDatabase db;
    public static Map<String, Boolean> validation_map;
    public static DatabaseHelper Attendance_db;
    private DialogFragment df;
    private Bundle bdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        Utility.setupUI(findViewById(R.id.parent_login_activity),this);
        initialize();
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        new DatabaseWork(this).delete_individual("","cse331");
    }

    protected void initialize() {

        df = new CreateDialog();
        bdl=new Bundle();
        Attendance_db = new DatabaseHelper(this);

        register_name_et = (EditText) findViewById(R.id.register_name_field_et);
        register_email_et = (EditText) findViewById(R.id.register_email_field_et);
        register_password_et = (EditText) findViewById(R.id.register_password_field_et);
        login_btn = (Button) findViewById(R.id.login_btn);
        register_btn = (Button) findViewById(R.id.register_btn);

        context = getApplicationContext();

        ToastMessage.toast_text = "Problem occured somewhere!!!";
        validation_map = new HashMap<String, Boolean>();

//        test for db work
//        String x = new DatabaseWork(context).show();
//        TextView test_tv = (TextView) findViewById(R.id.test_tv);
//        test_tv.setText(x);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the others_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loggin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.register_btn:
                validation_map.clear();
                validation_map.put("register_email_et", false);
                validation_map.put("register_password_et", false);
                validation_map.put("register_name_et", false);

                new addCustomTextChangedListener(this, register_name_et);
                new addCustomTextChangedListener(this, register_email_et);
                new addCustomTextChangedListener(this, register_password_et);

                if (validation_map.get("register_name_et") == true && validation_map.get("register_email_et") == true
                        && validation_map.get("register_password_et") == true) {

                    register_name_et_text = register_name_et.getText().toString().trim();
                    register_email_et_text = register_email_et.getText().toString().trim();
                    register_password_et_text = register_password_et.getText().toString().trim();


                    register_name_et.setText("");
                    register_email_et.setText("");
                    register_password_et.setText("");
                    new DatabaseWork(context).register_instructor(register_name_et_text, register_email_et_text, register_password_et_text);
                    ToastMessage.toast_text = "Registration completed.";
                } else {
                    ToastMessage.toast_text = "please, provide required information.";
                }

                ToastMessage.show_toast(LogginActivity.this, com.sust.attendence.Others.ToastMessage.toast_text);
                break;
            case R.id.login_btn:
                bdl.clear();
                bdl.putString("dialog_name", "login_dialog");
                df.setArguments(bdl);
                df.show(getSupportFragmentManager(), "dialog");
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Bundle bdll) {
        if(bdll.getBoolean("login_successful")) {
            Intent manu_intent = new Intent(LogginActivity.this, ManageActivity.class);
            startActivity(manu_intent);
            finish();
        }
        bdll.clear();
        bdl.clear();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, Bundle bdll) {
        bdll.clear();
        bdl.clear();
    }
}

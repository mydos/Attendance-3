package com.sust.attendence.Manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends Activity implements View.OnClickListener {
    private Spinner title_spinner;
    ArrayAdapter<String> spinner_adapter;
    private List<String> spinner_item;
    private Button create_title_btn, add_individual_btn;
    private EditText dialog_et_title;
    private String dialog_et_title_text;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        initialize();

    }

    protected void initialize() {
        session = new UserSessionManager(this);
        if (session.checkLogin())
            finish();
        title_spinner = (Spinner) findViewById(R.id.title_spinner);
        spinner_item = new ArrayList<String>();
        spinner_item = new DatabaseWork(this).get_title();
        spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinner_item);
        create_title_btn = (Button) findViewById(R.id.create_title_btn);
        add_individual_btn = (Button) findViewById(R.id.add_individual_btn);
        title_spinner.setAdapter(spinner_adapter);

        create_title_btn.setOnClickListener(this);
        add_individual_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_title_btn:
                appear_title_dialog();
                break;
            case R.id.add_individual_btn:
                break;
        }
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
                            new DatabaseWork(ManageActivity.this).insert_title(dialog_et_title_text);


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
}


package com.sust.attendence.Manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Listener.DialogListener;
import com.sust.attendence.Others.LogginActivity;
import com.sust.attendence.Others.MenuActivity;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

/**
 * Created by Ikhtiar on 6/3/2015.
 */
public class CreateDialog extends DialogFragment {

    private EditText dialog_et_title, dialog_et_ind_reg_no, dialog_et_ind_name, login_email_et, login_password_et;
    private TextView dialog_et_ind_inst_name, dialog_et_ind_title_name;
    private String dialog_et_title_text, dialog_et_ind_reg_no_text, dialog_et_ind_name_text, login_email_et_text, login_password_et_text;
    private int dialog_et_ind_reg_number,total_ind,present_ind,absent_ind;
    private Bundle bdl;
    DialogListener mListener;
    private String which_dialog;
    private Activity activity;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View layout;
    private UserSessionManager session;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        initialize();
        if (which_dialog == null) which_dialog = "default";
        switch (which_dialog) {
            case "create_title":
                return appear_title_dialog();
            case "add_individual":
                return appear_add_individual_Dialog();
            case "login_dialog":
                return login_dialog();
            case "save_roll_call":
                return save_roll_call();
            default:
                return appear();
        }

    }

    protected void initialize() {
        bdl = getArguments();
        which_dialog = bdl.getString("dialog_name");

        activity = getActivity();
        builder = new AlertDialog.Builder(activity);

        session = new UserSessionManager(activity);
    }

    public Dialog save_roll_call(){
        inflater = activity.getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_dialog_save_roll_call, null);
        builder.setView(layout);

        total_ind = bdl.getInt("total_individual");
        absent_ind = bdl.getInt("absent_individual");
        present_ind = total_ind - absent_ind;

        ((TextView) layout.findViewById(R.id.total_individual_tv)).setText("TOTAL : "+total_ind);
        ((TextView) layout.findViewById(R.id.present_tv)).setText("PRESENT : "+present_ind);
        ((TextView) layout.findViewById(R.id.absent_tv)).setText("ABSENT : "+absent_ind);


        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(CreateDialog.this, bdl);
                        ToastMessage.show_toast(activity, "SAVED");
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(CreateDialog.this, bdl);
                    }
                });
        return builder.create();
    }
    public Dialog appear() {
        builder.setMessage("ERROR IN DIALOG APPEARANCE!!!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(CreateDialog.this, bdl);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(CreateDialog.this, bdl);
                    }
                });
        return builder.create();
    }

    protected Dialog appear_title_dialog() {
        inflater = activity.getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_dialog_create_title, null);
        builder.setView(layout);

        dialog_et_title = (EditText) layout.findViewById(R.id.create_title_dialog_et);

        builder.setPositiveButton("CREATE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog_et_title_text = dialog_et_title.getText().toString().trim();
                        if (!dialog_et_title_text.equals("")) {
                            ToastMessage.toast_text = "Title Created Successfully!!!";
                            if (new DatabaseWork(activity).insert_title(dialog_et_title_text)) {

                                bdl.putString("dialog_et_title_text", dialog_et_title_text);

                            }

                        } else {
                            ToastMessage.toast_text = "Please Provide Title.";
                        }

                        ToastMessage.show_toast(activity, ToastMessage.toast_text);
                        mListener.onDialogPositiveClick(CreateDialog.this, bdl);
                        bdl.clear();
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(CreateDialog.this, bdl);
                        bdl.clear();
                    }
                });

        return builder.create();
    }

    protected Dialog appear_add_individual_Dialog() {
        // Get the layout inflater
        inflater = activity.getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_dialog_add_individual, null);
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
        dialog_et_ind_title_name.setText("TITLE NAME : " + bdl.getString("spinner_selected_item"));

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
                            new DatabaseWork(activity).add_individual(dialog_et_ind_reg_number, dialog_et_ind_name_text, bdl.getString("spinner_selected_item"));

                        } else {
                            ToastMessage.toast_text = "Please Provide required field.";
                        }
                        ToastMessage.show_toast(activity, ToastMessage.toast_text);
                        mListener.onDialogPositiveClick(CreateDialog.this, bdl);
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(CreateDialog.this, bdl);
                    }
                });

        return builder.create();

    }

    protected Dialog login_dialog() {

        // Get the layout inflater
        inflater = activity.getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_dialog, null);

        login_email_et = (EditText) layout
                .findViewById(R.id.login_email_feild_et);
        login_password_et = (EditText) layout
                .findViewById(R.id.login_password_feild_et);

        builder.setView(layout);

        builder.setPositiveButton("SUBMIT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        login_email_et_text = login_email_et.getText().toString().trim();
                        login_password_et_text = login_password_et.getText().toString().trim();


                        if (new DatabaseWork(activity).check_db_for_login(login_email_et_text, login_password_et_text)) {

                            bdl.putBoolean("login_successful",true);

                            ToastMessage.toast_text = "Login Successful.";

                        } else
                            ToastMessage.toast_text = "Email or Password do not match !!!";


                        ToastMessage.show_toast(activity, com.sust.attendence.Others.ToastMessage.toast_text);
                        mListener.onDialogPositiveClick(CreateDialog.this, bdl);
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(CreateDialog.this, bdl);
                    }
                });
        return builder.create();

    }

}

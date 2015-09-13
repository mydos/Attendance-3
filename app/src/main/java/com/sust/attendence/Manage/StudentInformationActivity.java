package com.sust.attendence.Manage;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sust.attendence.Adapter.Show_details_adapter;
import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Others.Absent_Record;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.Others.Utility;
import com.sust.attendence.R;

import java.util.ArrayList;

/**
 * Created by Ikhtiar on 7/28/2015.
 */
public class StudentInformationActivity extends Activity implements View.OnClickListener,TextView.OnEditorActionListener{
    TextView total_class_taken,count_present,count_absent,name_tv,reg_no_tv;
    private Show_details_adapter absent_record_adapter;
    private ListView details_list_view;
    private EditText name_edt,reg_no_edt;
    private int count_tt;
    private String name,title_name;
    private int reg_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);

        Utility.setupUI(findViewById(R.id.parent_std_info),this);
        initialize();
        grab_data();
    }

    protected void initialize(){
        total_class_taken = (TextView) findViewById(R.id.total_class_tv);
        count_present = (TextView) findViewById(R.id.present_tv);
        count_absent = (TextView) findViewById(R.id.absent_tv);
        name_tv = (TextView) findViewById(R.id.name_tv);
        reg_no_tv = (TextView) findViewById(R.id.reg_no_tv);
        name_edt =(EditText) findViewById(R.id.name_edit);
        reg_no_edt = (EditText) findViewById(R.id.reg_no_edit);

        details_list_view = (ListView) findViewById(R.id.absent_details_list);
        absent_record_adapter = null;
    }

    protected void grab_data(){
        Bundle bdl =  getIntent().getExtras();
        title_name = bdl.getString("title_name");
        name = bdl.getString("name");
        reg_no= Integer.parseInt(bdl.getString("reg_no"));

        name_edt.setText(name);
        reg_no_edt.setText(reg_no+"");

        name_edt.setOnClickListener(this);
        reg_no_edt.setOnClickListener(this);

        name_edt.setOnEditorActionListener(this);
        reg_no_edt.setOnEditorActionListener(this);

        count_tt = new DatabaseWork(this).total_class_taken(title_name);
        total_class_taken.setText("TOTAL OCCURRENCE  : " + count_tt);

        show_details();
    }
    public void show_details(){

        int std_id = new DatabaseWork(this).get_student_id(reg_no,title_name);
        ArrayList<Absent_Record> list = new DatabaseWork(this).get_absent_record(std_id,title_name);
        int count_ab= list.size();
        update_present_absent(count_ab);
        absent_record_adapter=new Show_details_adapter(this,R.layout.show_details_list_view,list);
        details_list_view.setAdapter(absent_record_adapter);
    }
    public void update_present_absent(int count_ab){
        count_absent.setText("ABSENT : "+count_ab);
        count_present.setText("PRESENT : "+ (count_tt-count_ab));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.name_edit:
                name_edt.setCursorVisible(true);
                break;
            case R.id.reg_no_edit:
                reg_no_edt.setCursorVisible(true);
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        int std_id=-999;
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                // the user is done typing.
                switch(v.getId()){
                    case R.id.name_edit:
                        name_edt.setCursorVisible(false);
                        Utility.hideSoftKeyboard(StudentInformationActivity.this);
                        if(!(name_edt.getText().toString().trim()).equals("")){
                            std_id = new DatabaseWork(this).get_student_id(reg_no,title_name);
                            if(std_id!=-999) {
                                new DatabaseWork(this).update_student_name(std_id, name_edt.getText().toString());
                                ToastMessage.show_toast(StudentInformationActivity.this, "Done Editing Name.");
                                return true; // consume.
                            }
                        }
                        else{
                            ToastMessage.show_toast(StudentInformationActivity.this, "Provide Valid Name.");
                            name_edt.setText(name);
                        }
                    case R.id.reg_no_edit:
                        reg_no_edt.setCursorVisible(false);
                        Utility.hideSoftKeyboard(StudentInformationActivity.this);
                        if(!(reg_no_edt.getText().toString().trim()).equals("")) {
                                std_id = new DatabaseWork(this).get_student_id(reg_no, title_name);
                                if (std_id != -999) {
                                    new DatabaseWork(this).update_student_id(std_id, reg_no_edt.getText().toString());

                                    ToastMessage.show_toast(StudentInformationActivity.this, "Done Editing ID.");
                                    return true; // consume.
                                }
                        }
                        else{
                            ToastMessage.show_toast(StudentInformationActivity.this, "Provide Valid ID.");
                            reg_no_edt.setText(reg_no+"");
                        }
                }
        }
        return false;
    }
}

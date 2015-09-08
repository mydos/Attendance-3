package com.sust.attendence.Manage;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sust.attendence.Adapter.Show_details_adapter;
import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Others.Absent_Record;
import com.sust.attendence.Others.Utility;
import com.sust.attendence.R;

import java.util.ArrayList;

/**
 * Created by Ikhtiar on 7/28/2015.
 */
public class StudentInformationActivity extends Activity {
    TextView total_class_taken,count_present,count_absent,name_tv,reg_no_tv;
    private Show_details_adapter absent_record_adapter;
    private ListView details_list_view;
    private int count_tt;

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

        details_list_view = (ListView) findViewById(R.id.absent_details_list);
        absent_record_adapter = null;
    }

    protected void grab_data(){
        Bundle bdl =  getIntent().getExtras();
        String title_name = bdl.getString("title_name");
        String name = bdl.getString("name");
        int reg_no= Integer.parseInt(bdl.getString("reg_no"));

        name_tv.setText("NAME : "+name);
        reg_no_tv.setText("ID : "+reg_no);

        count_tt = new DatabaseWork(this).total_class_taken(title_name);
        total_class_taken.setText("TOTAL OCCURRENCE  : " + count_tt);


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

}

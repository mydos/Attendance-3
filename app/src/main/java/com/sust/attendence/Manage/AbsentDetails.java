package com.sust.attendence.Manage;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.sust.attendence.Adapter.Show_details_adapter;
import com.sust.attendence.Others.Absent_Record;
import com.sust.attendence.Others.DataWrapper;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.Others.Utility;
import com.sust.attendence.R;

import java.util.ArrayList;

/**
 * Created by Ikhtiar on 9/21/2015.
 */
public class AbsentDetails extends AppCompatActivity {

    private ListView details_list_view;
    private Show_details_adapter absent_record_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_details);
         Utility.setupUI(findViewById(R.id.parent_details_info), this);
//        Utility.hideSoftKeyboard(AbsentDetails.this);
//        ToastMessage.show_toast(this,  " done ");
        initialize();
    }

    public void initialize(){
        details_list_view = (ListView) findViewById(R.id.absent_details_list);

        ArrayList<Absent_Record> list = getIntent().getParcelableArrayListExtra("list");
//        ToastMessage.show_toast(this,list.size()+" done ");
        absent_record_adapter=new Show_details_adapter(this,R.layout.show_details_list_view,list);
        details_list_view.setAdapter(absent_record_adapter);
    }
}

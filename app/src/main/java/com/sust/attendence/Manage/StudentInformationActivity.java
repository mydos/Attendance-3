package com.sust.attendence.Manage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sust.attendence.Adapter.Drawer_list_adapter;
import com.sust.attendence.Adapter.Extra_field_adapter;
import com.sust.attendence.Adapter.Show_details_adapter;
import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Listener.DialogListener;
import com.sust.attendence.Others.Absent_Record;
import com.sust.attendence.Others.DataWrapper;
import com.sust.attendence.Others.Drawer_item;
import com.sust.attendence.Others.Extra_Field;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.Others.Utility;
import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ikhtiar on 7/28/2015.
 */
public class StudentInformationActivity extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener,DialogListener{
    TextView total_class_taken,count_present,count_absent,name_tv,reg_no_tv;

    private EditText name_edt,reg_no_edt;
    private Button show_absent_details;
    private int count_tt;
    private String name,title_name;
    private int reg_no;
    private Bundle bdl;
    private ArrayList<Absent_Record> list;
    private ListView drawer_list,extra_field_list;
    private String[] drawer_list_text={"ADD FIELD","LOGOUT","EXIT"};
    private int[] drawer_list_image={R.drawable.ic_action_import,R.drawable.ic_action_logout,R.drawable.ic_action_cancel};
    private Drawer_list_adapter drawer_adapter_custom;
    private UserSessionManager session;
    private DialogFragment df;
    private int std_id;
    private Extra_field_adapter extra_field_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);

        Utility.setupUI(findViewById(R.id.parent_std_info), this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        check_session();
        initialize();
        grab_data();
        setDrawer();
        setupExtraField();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); //this method close current activity and return to previous
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void check_session() {
        session = new UserSessionManager(this);
        if (session.checkLogin())
            finish();
    }
    protected void initialize(){
        total_class_taken = (TextView) findViewById(R.id.total_class_tv);
        count_present = (TextView) findViewById(R.id.present_tv);
        count_absent = (TextView) findViewById(R.id.absent_tv);
        name_tv = (TextView) findViewById(R.id.name_tv);
        reg_no_tv = (TextView) findViewById(R.id.reg_no_tv);
        name_edt =(EditText) findViewById(R.id.name_edit);
        reg_no_edt = (EditText) findViewById(R.id.reg_no_edit);
        show_absent_details =(Button) findViewById(R.id.show_absent_details);
        drawer_list=(ListView)findViewById(R.id.left_drawer_sia);
        extra_field_list=(ListView)findViewById(R.id.extra_field_list);
        extra_field_adapter =null;


        show_absent_details.setOnClickListener(this);

    }
    protected void setupExtraField(){

        ArrayList<Extra_Field> extraFields = new ArrayList<>();
        extraFields = new DatabaseWork(this).getField(std_id);
        extra_field_adapter = new Extra_field_adapter(this,R.layout.extra_field_list_item,extraFields,title_name);
        extra_field_list.setAdapter(extra_field_adapter);
    }

    protected void setDrawer(){

        ArrayList<Drawer_item> item_list = new ArrayList<>();
        for(int i=0;i<drawer_list_text.length;i++){
            item_list.add(new Drawer_item(drawer_list_text[i],drawer_list_image[i]));
        }

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.drawer_list_header, drawer_list, false);


        drawer_adapter_custom = new Drawer_list_adapter(this,R.layout.drawer_list_item,item_list);
        drawer_list.addHeaderView(header,null,false);
        drawer_list.setAdapter(drawer_adapter_custom);

        drawer_list.setOnItemClickListener(new DrawerItemClickListener());
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    protected void selectItem(int position){

        switch(position){
            case 1:
                Bundle bdll =  new Bundle();
                df = new CreateDialog();
                bdll.putString("dialog_name", "create_field");
                df.setArguments(bdll);
                df.show(getSupportFragmentManager(), "dialog");
                break;
            case 2:
                session.logoutUser();
                finish();
                break;
            case 3:
                ActivityCompat.finishAffinity(this);
                break;
        }
    }

    protected void grab_data(){
        bdl =  getIntent().getExtras();
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

        std_id = new DatabaseWork(this).get_student_id(reg_no, title_name);
        list = new DatabaseWork(this).get_absent_record(std_id,title_name);

        int count_ab= list.size();
        update_present_absent(count_ab);

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
            case R.id.show_absent_details:
                if(!list.isEmpty() && !list.equals(null)) {
                    Intent i = new Intent(StudentInformationActivity.this, AbsentDetails.class);
                    i.putParcelableArrayListExtra("list", list);
                    startActivity(i);
                }
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Bundle bdll) {
        String str = bdll.getString("dialog_name");
        if(str!=null){
            switch(str){
                case "create_field":
                    String field_name=bdll.getString("dialog_et_title_text");
                    if (!field_name.equals(""))
                    {
                        if(new DatabaseWork(StudentInformationActivity.this).manage_field_for_all(title_name, field_name,"create_field")>0) {
                            extra_field_adapter.notifyDataSetChanged();
                            setupExtraField();
                            ToastMessage.toast_text = "Field Created Successfully!!!";
                        }
                        else{
                            ToastMessage.toast_text = "Please Provide Valid Field Name.";
                        }

                    }else{
                        ToastMessage.toast_text = "Please Provide Valid Field Name.";

                    }
                    ToastMessage.show_toast(StudentInformationActivity.this,ToastMessage.toast_text);
                    break;
            }
        }
        bdll.clear();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, Bundle bdl) {

    }
}

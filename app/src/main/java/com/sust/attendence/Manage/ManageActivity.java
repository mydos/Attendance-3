package com.sust.attendence.Manage;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v4.app.DialogFragment;

import com.sust.attendence.Adapter.Drawer_list_adapter;
import com.sust.attendence.Adapter.Listview_individual_adapter;
import com.sust.attendence.Adapter.Spinner_title_adapter;
import com.sust.attendence.Database.DatabaseWork;
import com.sust.attendence.Listener.DialogListener;
import com.sust.attendence.Others.Absent_Record;
import com.sust.attendence.Others.Drawer_item;
import com.sust.attendence.Others.Extra_Field;
import com.sust.attendence.Others.Individual_info;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ManageActivity extends FragmentActivity implements View.OnClickListener, DialogListener {
    private Spinner title_spinner;
    private Spinner_title_adapter spinner_adapter_custom;
    private Listview_individual_adapter listview_adapter_custom;
    private Drawer_list_adapter drawer_adapter_custom;
    private List<String> spinner_item;
    private Button create_title_btn, add_individual_btn,save_btn;
    private ToggleButton toggle_button;
    private EditText dialog_et_title, dialog_et_ind_reg_no, dialog_et_ind_name;
    private TextView dialog_et_ind_inst_name, dialog_et_ind_title_name;
    private String dialog_et_title_text, dialog_et_ind_name_text, spinner_selected_item, dialog_et_ind_reg_no_text;
    private int dialog_et_ind_reg_number;
    private UserSessionManager session;
    private ListView student_list,drawer_list;
    private SimpleCursorAdapter adapter;
    private ArrayAdapter<String> spinner_adapter;
    public static boolean pos[];
    public int reg_no[];
    private int total;
    private DialogFragment df;
    String[] objects = {"asd", "fgh", "jkl"};
    Bundle bdl;
    private static final int REQUEST_PICK_FILE = 1;
    private String[] drawer_list_text={"IMPORT","EXPORT","LOGOUT","EXIT"};
    private int[] drawer_list_image={R.drawable.ic_action_import,R.drawable.ic_action_export,R.drawable.ic_action_logout,R.drawable.ic_action_cancel};

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
        drawer_list = (ListView) findViewById(R.id.left_drawer);

        setDrawer();



        spinner_item = new ArrayList<String>();
        spinner_item = new DatabaseWork(this).get_title();

        spinner_adapter_custom = new Spinner_title_adapter(this, android.R.layout.simple_spinner_item, spinner_item);

//        spinner_adapter_custom.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
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
                if(spinner_selected_item==null){
                    ToastMessage.show_toast(this,"You have to create title.");
                    break;
                }
                if(!toggle_button.isChecked()){
                    try {
                        Intent intent = new Intent(this, FilePickerActivity.class);
                        startActivityForResult(intent, REQUEST_PICK_FILE);
                    }
                    catch(Exception e){
                        ToastMessage.show_toast(this,"Something goes wrong.");
                    }
                }
                else{
                    ToastMessage.show_toast(this,"You have to turn off the calling mode.");
                }
                break;
            case 2:
                if(spinner_selected_item==null){
                    ToastMessage.show_toast(this,"You have to create title.");
                    break;
                }
                if(listview_adapter_custom.isEmpty()){
                    ToastMessage.show_toast(this,"You have to add Individual.");
                    break;
                }
                if(!toggle_button.isChecked()){
                    try {
                        export_operation();


//                        String temp="";
//
//                        if(listview_adapter_custom.getCount()>0){
//
//                            for(int i=0;i<1;i++){
//                                Map<String,Boolean> map1=listview_adapter_custom.getItem(i).getPair();
//                                Map<String, Boolean> map = new TreeMap<>(map1);
//                                for (String k : map.keySet()) {
//                                    temp += k + " :  "+map.get(k).toString()  + "\n";
//                                }
//                                }
//                            }


                   ToastMessage.show_toast(ManageActivity.this,"DONE");

//                        ToastMessage.show_toast(this,"done");
                        }
                    catch(Exception e){
                        ToastMessage.show_toast(this,"Something goes wrong."+e.getMessage());
                    }
                }
                else{
                    ToastMessage.show_toast(this,"You have to turn off the calling mode.");
                }
                break;

            case 3:
                session.logoutUser();
                finish();
                break;
            case 4:
                finish();
                break;
        }
    }

    protected void export_operation(){
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/Pocket_Attendance");

        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date date = new Date();
        String file_name =spinner_selected_item+"-"+dateFormat.format(date);
        final String filename = folder.toString() + "/"+file_name+".csv";

        // show waiting screen
        CharSequence contentTitle = getString(R.string.app_name);
        final ProgressDialog progDailog = ProgressDialog.show(
                ManageActivity.this, contentTitle, "Proccessing...",
                true);//please wait


        new Thread() {
            public void run() {
                try {

                    FileWriter fw = new FileWriter(filename);


                    fw.append("REG");
                    fw.append(',');

                    fw.append("NAME");
                    fw.append(',');

                    //workspace


                   //setting timestamp to individual Info.
                   Map<String,ArrayList<String>> map = new DatabaseWork(ManageActivity.this).getPerformanceRecord(spinner_selected_item);

                   if(listview_adapter_custom.getCount()>0){
                       for(String key : map.keySet()){
                           for(int i=0;i<listview_adapter_custom.getCount();i++){
                               ArrayList<String> absent_reg_no= new ArrayList<>();
                               absent_reg_no = map.get(key);

                               if(absent_reg_no.contains(listview_adapter_custom.getItem(i).getReg_no()))
                                    listview_adapter_custom.getItem(i).setPair(key,Boolean.FALSE);
                               else{
                                    listview_adapter_custom.getItem(i).setPair(key,Boolean.TRUE);
                               }

                            }
                        }
                   }
                    //setting extra_field

                    int student_id;
                    String key,value;
                    ArrayList<Extra_Field> per_student_field_info;
                    Map<String,ArrayList<String>> all_student_field_info= new HashMap<String, ArrayList<String>>();
                    for(int i=0;i<listview_adapter_custom.getCount();i++){
                            student_id = new DatabaseWork(ManageActivity.this).get_student_id(
                                    Integer.parseInt(listview_adapter_custom.getItem(i).getReg_no()),spinner_selected_item);

                            if(student_id!=-999) {
                                per_student_field_info = new DatabaseWork(ManageActivity.this).getField(student_id);

                                for(int j=0;j<per_student_field_info.size();j++){
                                    key = per_student_field_info.get(j).getField_name();
                                    value = per_student_field_info.get(j).getField_value();
                                    if(all_student_field_info.get(key)==null){
                                        all_student_field_info.put(key,new ArrayList<String>());
                                    }
                                    all_student_field_info.get(key).add(value);
                                }
//
                            }
                  }



                    //writing to file


                    //sorting extra field
                    Map<String,ArrayList<String>> treeMap2 =new TreeMap<>(all_student_field_info);
                    //

                    String temp="";
                    boolean f=true;
                    int total=0;
                    if(listview_adapter_custom.getCount()>0){
                        Map<String,Boolean> map1;
                        for(int i=0;i<listview_adapter_custom.getCount();i++){

                            map1=listview_adapter_custom.getItem(i).getPair();
                            Map<String, Boolean> treeMap = new TreeMap<>(map1);

                            if(i!=0){
                                fw.append(listview_adapter_custom.getItem(i).getReg_no());
                                fw.append(',');

                                fw.append(listview_adapter_custom.getItem(i).getName());
                                fw.append(',');

                            }
                            for(String k: treeMap.keySet()){

                                if(i==0 && f){
                                    for(String j: treeMap.keySet()) {
                                        fw.append(j);
                                        fw.append(',');
                                    }
                                    total=new DatabaseWork(ManageActivity.this).total_class_taken(spinner_selected_item);
                                    fw.append("TOTAL PRESENT ("+total+")");
                                    fw.append(',');

                                    //here field name


                                    for(String ke : treeMap2.keySet()){
                                        fw.append(ke);
                                        fw.append(',');
                                    }
                                    //

                                    fw.append('\n');

                                    fw.append(listview_adapter_custom.getItem(i).getReg_no());
                                    fw.append(',');

                                    fw.append(listview_adapter_custom.getItem(i).getName());
                                    fw.append(',');


                                    f=false;
                                }
                                if(treeMap.get(k)==true){

                                    fw.append('P');
                                    fw.append(',');
                                }else{

                                    fw.append('A');
                                    fw.append(',');
                                }

//                                temp+=k+" :  "+map.get(k).toString()+"  "+map.size()+"\n";
                            }

                            int std_id = new DatabaseWork(ManageActivity.this).get_student_id(Integer.parseInt(listview_adapter_custom.getItem(i).getReg_no()),spinner_selected_item);
                            ArrayList<Absent_Record> list = new DatabaseWork(ManageActivity.this).get_absent_record(std_id,spinner_selected_item);
                            int count_p=total- list.size();

                            fw.append(count_p+"");
                            fw.append(',');



                            //here field value
                            for(String ke:treeMap2.keySet()){
                                fw.append(treeMap2.get(ke).get(i));
                                fw.append(',');
                            }


                            fw.append('\n');

                        }
                    }

//
//                    String temp=""+map.size();
//
//                    for (String k : map.keySet()) {
//                        temp += k + "   " +(map.get(k)).size()+"  ";
//
//                        for (int i = 0; i < map.get(k).size(); i++) {
//                            temp+=map.get(k).get(i);
//
//                        }
//                        temp="\n";
//                    }
////                   ToastMessage.show_toast(ManageActivity.this,"wah");

//                    fw.append(temp);
//                    fw.append(',');



//                    new DatabaseWork(ManageActivity.this).getPerformanceRecord(spinner_selected_item);
//                    ToastMessage.show_toast(ManageActivity.this,ToastMessage.toast_text);
//
//                    if(listview_adapter_custom.getCount()>0){
//                        for(int i=0;i<listview_adapter_custom.getCount();i++){
//
//                            listview_adapter_custom.getItem(i).setPair("",Boolean.TRUE);
//                        }
//                    }
                    //workspace

                    fw.append('\n');



                    // fw.flush();
                    fw.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }

        }.start();

        timerDelayRemoveDialog(1000,progDailog);
    }

    public void timerDelayRemoveDialog(long time, final Dialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);
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
                String reg_no = ((TextView)view.findViewById(R.id.display_reg)).getText().toString().trim();
                bdl.putString("reg_no",reg_no);
                String name = ((TextView)view.findViewById(R.id.display_name)).getText().toString().trim();
                bdl.putString("name",name);
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
            reg_no = new int[listview_adapter_custom.getCount()];
            for (int i = 0; i < listview_adapter_custom.getCount(); i++) {
                pos[i] = false;
                reg_no[i]=0;
            }
        }
    }

    protected void manage_listitem() {
        if (toggle_button.isChecked() && listview_adapter_custom.getCount()>0) {
            save_btn.setVisibility(View.VISIBLE);
            set_listitem_position();
            student_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (pos[position]) {
                        view.setBackgroundColor(Color.TRANSPARENT);
                        pos[position] = false;
                        reg_no[position]=0;
                        --total;
                    } else {
                        view.setBackgroundColor(Color.parseColor("#fea6a6"));
                        pos[position] = true;
                        reg_no[position]=Integer.parseInt(((TextView)view.findViewById(R.id.display_reg)).getText().toString());
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
                if(listview_adapter_custom!=null) {
                    manage_listitem();
                }else{
                    toggle_button.setChecked(false);
                    ToastMessage.show_toast(ManageActivity.this,"You have to create title and add individual to perform call.");
                }
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

//
//
//                InputStream inputStream = null;
//                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
//                try {
//
//                    inputStream = getApplicationContext().getAssets().open("teSt.docx");
//
//                    int i = inputStream.read();
//                    while (i!=-1) {
//                        outputStream.write(i);
//                        i = inputStream.read();
//                    }
//                    inputStream.close();
//                    }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                ToastMessage.show_toast(ManageActivity.this,"OK   :   "+outputStream);
//                break;

        }
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
                        new DatabaseWork(this).update_absent_record_if_exist(new DatabaseWork(this).get_student_id(bdll.getInt("reg_no"),spinner_selected_item),spinner_selected_item);
                        show_student_list();
                    break;
                case "save_roll_call":
                    toggle_button.setChecked(false);
                    long freq_id=new DatabaseWork(this).update_attendence_frequency(spinner_selected_item);
                    String v="reg_ no \n";
                    for(int i=0;i<listview_adapter_custom.getCount();i++){
                        if(reg_no[i]!=0 && freq_id!=-1){
                            int std_id = new DatabaseWork(this).get_student_id(reg_no[i],spinner_selected_item);
                            long id=new DatabaseWork(this).update_absent_record(std_id,freq_id);
                        }
                    }

                    ToastMessage.show_toast(this,ToastMessage.toast_text+v);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_PICK_FILE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String str_path=data.getStringExtra("file_path");
                import_operation(str_path);
                ToastMessage.show_toast(this,str_path);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        show_student_list();
    }

    protected void import_operation(String path){
        String line="",str="";
        BufferedReader reader=null;
        ArrayList<Individual_info> std_info = new ArrayList<Individual_info>();
        CharSequence contentTitle = getString(R.string.app_name);
        final ProgressDialog progDailog = ProgressDialog.show(
                ManageActivity.this, contentTitle, "Proccessing...",
                true);//please wait

        try{
            reader = new BufferedReader(new FileReader(path));

            while((line = reader.readLine())!=null){

                String[] cols =line.split(",");
                std_info.add(new Individual_info(cols[0],cols[1]));
            }
            if(std_info.size()>0) {
                new DatabaseWork(this).import_operation_add(std_info, spinner_selected_item);
                show_student_list();
                timerDelayRemoveDialog(1000,progDailog);
                ToastMessage.show_toast(this, "Import Operattion Succcessful.");

            }
            else{
                progDailog.dismiss();
                ToastMessage.show_toast(this, "FILE IS EMPTY!");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            progDailog.dismiss();
            ToastMessage.show_toast(this,str+"PLEASE SELECT CSV OR TXT EXTENSION.");
        }
    }

}

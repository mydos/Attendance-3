package com.sust.attendence.Database;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;

import com.sust.attendence.Manage.ManageActivity;
import com.sust.attendence.Others.Absent_Record;
import com.sust.attendence.Others.Individual_info;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.Session.UserSessionManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Ikhtiar on 5/12/2015.
 */
public class DatabaseWork {

    private SQLiteDatabase db;
    private DatabaseHelper Attendance_db;
    private Context context;
    private UserSessionManager session;

    public DatabaseWork(Context context) {
        this.context = context;
        initialize();

    }

    public void initialize() {
        Attendance_db = new DatabaseHelper(context);
        session = new UserSessionManager(context);
    }

    public boolean check_db_for_login(String email_et_text, String password_et_text) {
        db = Attendance_db.getReadableDatabase();

        String[] projection = {Contract.Entry._ID, Contract.Entry.INSTRUCTOR_COLUMN_NAME_1,
                Contract.Entry.INSTRUCTOR_COLUMN_NAME_2, Contract.Entry.INSTRUCTOR_COLUMN_NAME_3,};

        String selection = Contract.Entry.INSTRUCTOR_COLUMN_NAME_2 + "=? AND "
                + Contract.Entry.INSTRUCTOR_COLUMN_NAME_3 + "=?";

        String[] selectionArgs = {email_et_text, password_et_text};
        Cursor c = db.query(Contract.Entry.INSTRUCTOR_TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        c.moveToFirst();
        if (c.getCount() > 0) {
            session.createUserLoginSession(c.getString(1), c.getInt(0));
        }

        return c.getCount() > 0 ? true : false;
    }

    public int total_class_taken(String title_name){
        db = Attendance_db.getReadableDatabase();

        String[] projection = {Contract.Entry_attendance_frequency._ID, Contract.Entry_attendance_frequency.COLUMN_NAME_1,
                Contract.Entry_attendance_frequency.COLUMN_NAME_2,Contract.Entry_attendance_frequency.COLUMN_NAME_3};

        String selection = Contract.Entry_attendance_frequency.COLUMN_NAME_1 + "=? AND " +
                Contract.Entry_attendance_frequency.COLUMN_NAME_2 + "=?";

        String[] selectionArgs = {session.get_inst_id() + "", title_name + ""};

        Cursor c = db.query(Contract.Entry_attendance_frequency.TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
    return c.getCount();
    }
    public boolean delete_title(String title_name) {
        db = Attendance_db.getReadableDatabase();

        String selection = Contract.Entry_title.TITLE_COLUMN_NAME_2 + " LIKE ?";
        String[] selectionArgs = {title_name};


        int count = db.delete(
                Contract.Entry_title.TITLE_TABLE_NAME,
                selection,
                selectionArgs
        );
//        ToastMessage.show_toast(context,title_name+ " row : "+count);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean delete_individual(String reg_no, String title_name) {
        db = Attendance_db.getReadableDatabase();

        String selection = Contract.Entry_students.STUDENT_COLUMN_NAME_1 + "=? AND "
                + Contract.Entry_students.STUDENT_COLUMN_NAME_3 + "=? ";
        String[] selectionArgs = {reg_no,title_name};


        int count = db.delete(
                Contract.Entry_students.STUDENT_TABLE_NAME,
                selection,
                selectionArgs
        );
//        ToastMessage.show_toast(context,title_name+ " row : "+count);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    public long update_attendence_frequency(String spinner_selected_item) {
        db = Attendance_db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Entry_attendance_frequency.COLUMN_NAME_1, session.get_inst_id());
        values.put(Contract.Entry_attendance_frequency.COLUMN_NAME_2, spinner_selected_item);

        long id=-1;
        try {
            id=db.insertOrThrow(
                    Contract.Entry_attendance_frequency.TABLE_NAME,
                    null,
                    values);
            ToastMessage.toast_text = "SUCCESS ENTRY.";
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage.toast_text = "SORRY!! ERROR IN INPUT!!." + e.getMessage();
        }
        return id;
    }

    public long update_absent_record(int std_id,long freq_id){
        db = Attendance_db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Entry_absent_record.COLUMN_NAME_1, std_id);
        values.put(Contract.Entry_absent_record.COLUMN_NAME_2, freq_id);
        values.put(Contract.Entry_absent_record.COLUMN_NAME_3, "comments");
        long id=-1;
        try {
            id=db.insertOrThrow(
                    Contract.Entry_absent_record.TABLE_NAME,
                    null,
                    values);
            ToastMessage.toast_text = "SUCCESS ENTRY.:)";
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage.toast_text = "SORRY!! ERROR IN INPUT!!.:("+e.getMessage();
        }
        return id;
    }

    public void update_absent_record_if_exist(int std_id,String title_name){
        db = Attendance_db.getWritableDatabase();

        String[] projection = {Contract.Entry_attendance_frequency._ID, Contract.Entry_attendance_frequency.COLUMN_NAME_1,
                Contract.Entry_attendance_frequency.COLUMN_NAME_2,Contract.Entry_attendance_frequency.COLUMN_NAME_3};

        String selection = Contract.Entry_attendance_frequency.COLUMN_NAME_1 + "=? AND " +
                Contract.Entry_attendance_frequency.COLUMN_NAME_2 + "=?";

        String[] selectionArgs = {session.get_inst_id() + "", title_name + ""};

        Cursor c = db.query(Contract.Entry_attendance_frequency.TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        c.moveToFirst();
        String v="";

        for(int i=0;i<c.getCount();i++){
            update_absent_record(std_id,c.getInt(0));
            v+=c.getInt(0)+" \n";
            c.moveToNext();
        }

        ToastMessage.show_toast(context,v);

    }
    public boolean set_as_present(int id){
        db = Attendance_db.getReadableDatabase();

        String selection = Contract.Entry_absent_record._ID + "=? ";
        String[] selectionArgs = {id+""};

        int count = db.delete(
                Contract.Entry_absent_record.TABLE_NAME,
                selection,
                selectionArgs
        );
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    public ArrayList<Absent_Record> get_absent_record(int std_id,String title_name){
        db= Attendance_db.getReadableDatabase();
        String query ="SELECT TIMESTAMP_PER_CALL,COMMENTS,AR._ID from ATTENDANCE_FREQUENCY as AF inner join ABSENT_RECORD as AR " +
                "where AR.ATTENDANCE_FREQUENCY_ID = AF._ID and TITLE_NAME = '"+title_name+"' and AR.STUDENT_ID = "+std_id+"";
        Cursor c;
        ArrayList<Absent_Record> list = new ArrayList<>();

        try{
            c=db.rawQuery(query,null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                list.add(new Absent_Record(Timestamp.valueOf(c.getString(0)),c.getString(1),c.getInt(2)));
                c.moveToNext();
            }
            return list;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int get_frequency_id(String title_name){
        db = Attendance_db.getReadableDatabase();

        String[] projection = {Contract.Entry_attendance_frequency._ID};

        String selection = Contract.Entry_attendance_frequency.COLUMN_NAME_1 + "=? AND " +
                Contract.Entry_attendance_frequency.COLUMN_NAME_2 + "=?";

        String[] selectionArgs = {session.get_inst_id() + "", title_name + ""};

        Cursor c = db.query(Contract.Entry_attendance_frequency.TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        return c.getCount();
    }
    public int get_student_id(int reg_no,String title_name){
        db=Attendance_db.getReadableDatabase();
        String[] projection = {Contract.Entry_students._ID};

        String selection = Contract.Entry_students.STUDENT_COLUMN_NAME_1 + "=? AND " +
                Contract.Entry_students.STUDENT_COLUMN_NAME_2 + "=? AND " +
                Contract.Entry_students.STUDENT_COLUMN_NAME_3 + "=?";

        String[] selectionArgs = {reg_no+"",session.get_inst_id() + "", title_name + ""};

        Cursor c = db.query(Contract.Entry_students.STUDENT_TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        int id=-999;
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            id = c.getInt(0);
            c.moveToNext();
        }
        return id;
    }

    // FOR TESTING PURPOSES
    //********************
    //********************
    //********************
    //********************
    //********************
    public void TEST_af(String spinner_selected_item,int inst_id) {
        db = Attendance_db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Entry_attendance_frequency.COLUMN_NAME_1,inst_id);
        values.put(Contract.Entry_attendance_frequency.COLUMN_NAME_2,spinner_selected_item);

        try {
            db.insertOrThrow(
                    Contract.Entry_attendance_frequency.TABLE_NAME,
                    null,
                    values);
            ToastMessage.toast_text = "af SUCCESS ENTRY.";
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage.toast_text = "af SORRY!! ERROR IN INPUT!!."+e.getMessage();
        }
    }
    public void TEST_ar() {
        db = Attendance_db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Entry_absent_record.COLUMN_NAME_1, 1);
        values.put(Contract.Entry_absent_record.COLUMN_NAME_2, 12);
        values.put(Contract.Entry_absent_record.COLUMN_NAME_3, "comments");
        try {
            db.insertOrThrow(
                    Contract.Entry_absent_record.TABLE_NAME,
                    null,
                    values);
            ToastMessage.toast_text = "SUCCESS ENTRY.:)";
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage.toast_text = "SORRY!! ERROR IN INPUT!!.:("+e.getMessage();
        }
    }

    public String show_test_af() {
        db = Attendance_db.getReadableDatabase();

        String[] projection = {Contract.Entry_attendance_frequency._ID, Contract.Entry_attendance_frequency.COLUMN_NAME_1,
                Contract.Entry_attendance_frequency.COLUMN_NAME_2,Contract.Entry_attendance_frequency.COLUMN_NAME_3};

        Cursor c = db.query(Contract.Entry_attendance_frequency.TABLE_NAME, // The table to query
                projection, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        String x = "ATTENDANCE_FREQUENCY \n";
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {

            //x += " " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2)  + "\n";
            x += " " + c.getInt(0) + " " + c.getInt(1) + " " + c.getString(2) + " " + c.getString(3)  + "\n";
            c.moveToNext();
        }
        return x;
    }

    public String show_test_ar() {
        String x = "ABSENT RECORD \n";
        try{
            db = Attendance_db.getReadableDatabase();

            String[] projection = {Contract.Entry_absent_record._ID, Contract.Entry_absent_record.COLUMN_NAME_1,
                Contract.Entry_absent_record.COLUMN_NAME_2,Contract.Entry_absent_record.COLUMN_NAME_3};

            Cursor c = db.query(Contract.Entry_absent_record.TABLE_NAME, // The table to query
                projection, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {

            x += " " + c.getInt(0) + " " + c.getInt(1) + " " + c.getInt(2)+ " " + c.getString(3)  + "\n";
            c.moveToNext();
        }
        }
        catch(Exception e){
            x += e.getMessage().toString();
        }
        return x;
    }

    //*********************
    //*********************
    //*********************
    //*********************
    //*********************
    // FOR TESTING PURPOSES

    public void add_individual(int reg_no, String students_name, String title_name) {
        db = Attendance_db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_1, reg_no);
        values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_2, session.get_inst_id());
        values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_3, title_name);
        values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_4, students_name);

        try {
            db.insertOrThrow(
                    Contract.Entry_students.STUDENT_TABLE_NAME,
                    null,
                    values);
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage.toast_text = "SORRY!! THIS REG IS ALREADY INSERTED!!.";
        }
    }

    public void import_operation_add(ArrayList<Individual_info> std_info, String title_name) {
        db = Attendance_db.getWritableDatabase();

        ContentValues values = new ContentValues();

        for (int i = 0; i < std_info.size(); i++) {
            values.clear();
            values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_1, Integer.parseInt(std_info.get(i).getReg_no()));
            values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_2, session.get_inst_id());
            values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_3, title_name);
            values.put(Contract.Entry_students.STUDENT_COLUMN_NAME_4, std_info.get(i).getName());

            try {
                int std_id = get_student_id(Integer.parseInt(std_info.get(i).getReg_no()),title_name);

                long id = db.insertWithOnConflict(
                        Contract.Entry_students.STUDENT_TABLE_NAME,
                        null,
                        values,
                        db.CONFLICT_IGNORE);

                if((int)id!=std_id)
                     update_absent_record_if_exist((int)id,title_name);

            } catch (Exception e) {
                e.printStackTrace();
                ToastMessage.toast_text = "SORRY!! INVALID INPUT!!.";
            }

        }
    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public Map<String,ArrayList<String>> getPerformanceRecord(String title_name){
        db = Attendance_db.getReadableDatabase();

        String query ="select timestamp_per_call,registration_no from ATTENDANCE_FREQUENCY as af left outer join " +
                "(select attendance_frequency_id,name,registration_no from STUDENTS as s inner join ABSENT_RECORD as arr " +
                "where s._id = arr.student_id) as ar on af._id=ar.attendance_frequency_id where " +
                "instructor_id='"+session.get_inst_id()+"' and " +
                "title_name='"+title_name+"' order by timestamp_per_call asc";
        Cursor c;

        Map<String,ArrayList<String>> map = new HashMap<>();
        ArrayList<String> str= new ArrayList<>();
        String temp="";
        int x=0;
        String s="";
        String key,value;
        try{
            c=db.rawQuery(query,null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){

              key=c.getString(0);
              value=c.getString(1);

                if(map.get(key)==null){
                    map.put(key,new ArrayList<String>());
                }
                map.get(key).add(value);

                c.moveToNext();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            ToastMessage.toast_text="error"+e.getMessage();
        }

        Map<String, ArrayList<String>> treeMap = new TreeMap<>(map);

//        temp="";
//
//        for (String k : treeMap.keySet()) {
//            temp += k + "   " +(treeMap.get(k)).size()+"  ";
//
//            for (int i = 0; i < treeMap.get(k).size(); i++) {
//                temp+=treeMap.get(k).get(i)+" : ";
//            }
//            temp+="\n";
//        }

//        for(Map.Entry<String, ArrayList<String>> alternateEntry : map.entrySet()) {
//            temp+=alternateEntry.getKey() + " : " +
//                    alternateEntry.getValue().toString()+"\n";
//        }
//        ToastMessage.toast_text=temp+"SIZE : "+map.size()+"   "+x;


        return treeMap;
    }
    public ArrayList<Individual_info> get_student_list(String title_name) {
        ArrayList<Individual_info> individual_info = new ArrayList<Individual_info>();

        db = Attendance_db.getReadableDatabase();

        String[] projection = {Contract.Entry_students._ID, Contract.Entry_students.STUDENT_COLUMN_NAME_1, Contract.Entry_students.STUDENT_COLUMN_NAME_2,
                Contract.Entry_students.STUDENT_COLUMN_NAME_3, Contract.Entry_students.STUDENT_COLUMN_NAME_4};

        String selection = Contract.Entry_students.STUDENT_COLUMN_NAME_2 + "=? AND " +
                Contract.Entry_students.STUDENT_COLUMN_NAME_3 + "=?";

        String[] selectionArgs = {session.get_inst_id() + "", title_name + ""};

        Cursor c = db.query(Contract.Entry_students.STUDENT_TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        c.moveToFirst();


        for (int i = 0; i < c.getCount(); i++) {
            individual_info.add(new Individual_info(c.getString(1), c.getString(4)));
            c.moveToNext();
        }

        return individual_info;
    }

    public void register_instructor(String register_name_et_text, String register_email_et_text, String register_password_et_text) {
        db = Attendance_db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.INSTRUCTOR_COLUMN_NAME_1, register_name_et_text);
        values.put(Contract.Entry.INSTRUCTOR_COLUMN_NAME_2, register_email_et_text);
        values.put(Contract.Entry.INSTRUCTOR_COLUMN_NAME_3, register_password_et_text);

        try {
            db.insertOrThrow(
                    Contract.Entry.INSTRUCTOR_TABLE_NAME,
                    null,
                    values);
        } catch (Exception e) {
            ToastMessage.toast_text = "SORRY!! THIS ID IS ALREADY STORED.";
        }
    }

    public boolean insert_title(String dialog_et_title_text) {
        db = Attendance_db.getWritableDatabase();
        boolean insert_flag = false;
        ContentValues values = new ContentValues();
        values.put(Contract.Entry_title.TITLE_COLUMN_NAME_1, session.get_inst_id());
        values.put(Contract.Entry_title.TITLE_COLUMN_NAME_2, dialog_et_title_text);

        try {
            db.insertOrThrow(
                    Contract.Entry_title.TITLE_TABLE_NAME,
                    null,
                    values);
            insert_flag = true;
        } catch (Exception e) {
            ToastMessage.toast_text = "SORRY!! THIS TITLE IS ALREADY STORED.";
        }
        return insert_flag;
    }

    public ArrayList<String> get_title() {
        db = Attendance_db.getReadableDatabase();
        ArrayList<String> title_list = new ArrayList<String>();

        String[] projection = {Contract.Entry_title.TITLE_COLUMN_NAME_2};

        String selection = Contract.Entry_title.TITLE_COLUMN_NAME_1 + "=?";

        String[] selectionArgs = {session.get_inst_id() + ""};

        Cursor c = db.query(Contract.Entry_title.TITLE_TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            title_list.add(c.getString(0));
            c.moveToNext();
        }
        return title_list;
    }

    public void delete_instructor() {
//        // Define 'where' part of query.
//        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
//// Specify arguments in placeholder order.
//        String[] selectionArgs = {String.valueOf(rowId)};
//// Issue SQL statement.
//        db.delete(table_name, selection, selectionArgs);
    }

    public String show() {
        db = Attendance_db.getReadableDatabase();

        String[] projection = {Contract.Entry._ID, Contract.Entry.INSTRUCTOR_COLUMN_NAME_1,
                Contract.Entry.INSTRUCTOR_COLUMN_NAME_2, Contract.Entry.INSTRUCTOR_COLUMN_NAME_3,};

        Cursor c = db.query(Contract.Entry.INSTRUCTOR_TABLE_NAME, // The table to query
                projection, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        String x = "INSTRUCTOR \n";
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {

            x += " " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + "\n";
            c.moveToNext();
        }

        projection = new String[]{Contract.Entry_title.TITLE_COLUMN_NAME_1, Contract.Entry_title.TITLE_COLUMN_NAME_2};

        c = db.query(Contract.Entry_title.TITLE_TABLE_NAME, // The table to query
                projection, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        x += "\n TITLE \n";
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {

            x += " " + c.getString(0) + " " + c.getString(1) + "\n";
            c.moveToNext();
        }

        projection = new String[]{Contract.Entry_students._ID, Contract.Entry_students.STUDENT_COLUMN_NAME_1, Contract.Entry_students.STUDENT_COLUMN_NAME_2,
                Contract.Entry_students.STUDENT_COLUMN_NAME_3, Contract.Entry_students.STUDENT_COLUMN_NAME_4};

        c = db.query(Contract.Entry_students.STUDENT_TABLE_NAME, // The table to query
                projection, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        x += "\n STUDENTS \n";
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {

            x += " " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + "\n";
            c.moveToNext();
        }

        ToastMessage.toast_text = x + "    c    =  " + c;
        ToastMessage.show_toast(context, ToastMessage.toast_text);
        return x;
    }
}

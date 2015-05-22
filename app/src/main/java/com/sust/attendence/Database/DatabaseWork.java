package com.sust.attendence.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.Session.UserSessionManager;

import java.util.ArrayList;

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
            ToastMessage.toast_text = "SORRY!! ERROR IN INPUT!!.";
        }
    }

    public Cursor get_student_list(String title_name) {
        db = Attendance_db.getReadableDatabase();

        String[] projection = {Contract.Entry_students._ID,Contract.Entry_students.STUDENT_COLUMN_NAME_1, Contract.Entry_students.STUDENT_COLUMN_NAME_2,
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
        return c;
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
        boolean insert_flag =false;
        ContentValues values = new ContentValues();
        values.put(Contract.Entry_title.TITLE_COLUMN_NAME_1, session.get_inst_id());
        values.put(Contract.Entry_title.TITLE_COLUMN_NAME_2, dialog_et_title_text);

        try {
            db.insertOrThrow(
                    Contract.Entry_title.TITLE_TABLE_NAME,
                    null,
                    values);
            insert_flag =true;
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

        projection = new String[]{Contract.Entry_students._ID,Contract.Entry_students.STUDENT_COLUMN_NAME_1, Contract.Entry_students.STUDENT_COLUMN_NAME_2,
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

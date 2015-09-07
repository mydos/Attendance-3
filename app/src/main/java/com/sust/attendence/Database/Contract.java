package com.sust.attendence.Database;

import android.provider.BaseColumns;

public class Contract {
    public Contract() {
    }

    public static abstract class Entry implements BaseColumns {
        public static final String INSTRUCTOR_TABLE_NAME = "INSTRUCTOR";
        public static final String INSTRUCTOR_COLUMN_NAME_1 = "INSTRUCTOR_NAME";
        public static final String INSTRUCTOR_COLUMN_NAME_2 = "INSTRUCTOR_EMAIL";
        public static final String INSTRUCTOR_COLUMN_NAME_3 = "INSTRUCTOR_PASSWORD";
    }

    public static abstract class Entry_title{

        public static final String TITLE_TABLE_NAME = "TITLE";
        public static final String TITLE_COLUMN_NAME_1 = "INSTRUCTOR_ID";
        public static final String TITLE_COLUMN_NAME_2  = "TITLE_NAME";
    }
    public static abstract class Entry_students implements BaseColumns{
        public static final String STUDENT_TABLE_NAME = "STUDENTS";
        public static final String STUDENT_COLUMN_NAME_1 = "REGISTRATION_NO";
        public static final String STUDENT_COLUMN_NAME_2 = "INSTRUCTOR_ID";
        public static final String STUDENT_COLUMN_NAME_3 = "TITLE_NAME";
        public static final String STUDENT_COLUMN_NAME_4  = "NAME";
    }

    public static abstract class Entry_attendance_frequency implements BaseColumns {
        public  static  final String TABLE_NAME = "ATTENDANCE_FREQUENCY";
        public  static  final String COLUMN_NAME_1 = "INSTRUCTOR_ID";
        public  static  final String COLUMN_NAME_2 = "TITLE_NAME";
        public  static  final String COLUMN_NAME_3 = "TIMESTAMP_PER_CALL";

    }

    public static abstract class Entry_absent_record implements BaseColumns {
        public  static  final String TABLE_NAME = "ABSENT_RECORD";
        public  static  final String COLUMN_NAME_1 = "STUDENT_ID";
        public  static  final String COLUMN_NAME_2 = "ATTENDANCE_FREQUENCY_ID";
        public  static  final String COLUMN_NAME_3 = "COMMENTS";
    }
}

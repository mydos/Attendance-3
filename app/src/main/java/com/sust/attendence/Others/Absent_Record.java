package com.sust.attendence.Others;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Ikhtiar on 7/28/2015.
 */
public class Absent_Record implements Parcelable {


    private Timestamp timestamp;
    private String comments;
    private int db_id;

    public Absent_Record(Timestamp ts,String cm,int db_id) {
        this.timestamp=ts;
        this.comments=cm;
        this.db_id=db_id;
    }

    protected Absent_Record(Parcel in) {
        timestamp =Timestamp.valueOf(in.readString());
        comments = in.readString();
        db_id = in.readInt();
    }

    public static final Creator<Absent_Record> CREATOR = new Creator<Absent_Record>() {
        @Override
        public Absent_Record createFromParcel(Parcel in) {
            return new Absent_Record(in);
        }

        @Override
        public Absent_Record[] newArray(int size) {
            return new Absent_Record[size];
        }
    };

    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(timestamp.toString());
        parcel.writeString(comments);
        parcel.writeInt(db_id);
    }
}

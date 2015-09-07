package com.sust.attendence.Others;

import java.sql.Timestamp;

/**
 * Created by Ikhtiar on 7/28/2015.
 */
public class Absent_Record {


    private Timestamp timestamp;
    private String comments;
    private int db_id;

    public Absent_Record(Timestamp ts,String cm,int db_id) {
        this.timestamp=ts;
        this.comments=cm;
        this.db_id=db_id;
    }

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

}

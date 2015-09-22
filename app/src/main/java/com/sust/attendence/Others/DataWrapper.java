package com.sust.attendence.Others;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ikhtiar on 9/21/2015.
 */
public class DataWrapper implements Serializable {

    private ArrayList<Absent_Record> list;

    public DataWrapper(ArrayList<Absent_Record> list) {
        this.list = list;
    }

    public ArrayList<Absent_Record> getRecord() {
        return this.list;
    }

}
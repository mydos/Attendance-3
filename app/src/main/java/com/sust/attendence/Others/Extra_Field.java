package com.sust.attendence.Others;

/**
 * Created by Ikhtiar on 9/23/2015.
 */
public class Extra_Field {
    int field_id;
    String field_name;
    String field_value;

    public Extra_Field(int field_id, String field_name, String field_value) {
        this.field_id = field_id;
        this.field_name = field_name;
        this.field_value = field_value;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getField_value() {
        return field_value;
    }

    public void setField_value(String field_value) {
        this.field_value = field_value;
    }
}

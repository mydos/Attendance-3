package com.sust.attendence.Others;

/**
 * Created by Ikhtiar on 6/10/2015.
 */
public class Individual_info {
    private String reg_no;
    private String name;

    public Individual_info(String reg_no, String name) {
        this.reg_no = reg_no;
        this.name = name;
    }

    public String getReg_no() {
        return reg_no;
    }


    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

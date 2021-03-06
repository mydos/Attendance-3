package com.sust.attendence.Others;

/**
 * Created by Ikhtiar on 6/10/2015.
 */
import com.sust.attendence.Manage.ManageActivity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Individual_info {
    private String reg_no;
    private String name;
    private Map<String,Boolean> pair;

    public Individual_info(String reg_no, String name) {
        this.reg_no = reg_no;
        this.name = name;
        this.pair=null;
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

    public static Comparator<Individual_info> Comparator = new Comparator<Individual_info>() {

        public int compare(Individual_info lhs, Individual_info rhs) {
            int reg1 = Integer.parseInt(lhs.getReg_no());
            int reg2 = Integer.parseInt(rhs.getReg_no());
            return reg1-reg2;
        }};

    public void setPair(String timestamp,Boolean value){
        if(pair==null){
            pair=new HashMap<String,Boolean>();
        }
        Boolean v = new Boolean(value);
        pair.put(timestamp,v);
    }
    public Map<String,Boolean> getPair(){
        return pair;
    }
}

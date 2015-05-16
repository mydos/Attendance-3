package com.sust.attendence.Others;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ikhtiar on 5/12/2015.
 */
public class ToastMessage {
    public static String toast_text;
    public static int toast_duration;

    public static void show_toast(Context context, CharSequence toast_text) {
        toast_duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toast_text, toast_duration);
        toast.show();

    }
}

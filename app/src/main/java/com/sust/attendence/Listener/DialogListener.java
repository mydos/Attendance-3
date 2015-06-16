package com.sust.attendence.Listener;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Ikhtiar on 6/5/2015.
 */
public interface DialogListener {
    public void onDialogPositiveClick(DialogFragment dialog, Bundle bdl);
    public void onDialogNegativeClick(DialogFragment dialog, Bundle bdl);

}

package com.swiftsynq.journalapp.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by popoolaadebimpe on 26/06/2018.
 */

public class TimePickerFragment extends DialogFragment{

    private  int timeHour;
    private int timeMinute;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        timeHour = c.get(Calendar.HOUR);
        timeMinute = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), timeHour, timeMinute, false);
    }
}

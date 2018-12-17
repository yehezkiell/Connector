package com.example.yehezkiel.eclassapp;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Yehezkiel on 11/12/2018.
 */

public class myValueFormater implements IValueFormatter {

    private DecimalFormat mFormat;

    public myValueFormater() {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value); // e.g. append a dollar-sign
    }

}

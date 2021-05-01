package com.example.ready.Graph;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class XAxisValueFormatter extends IndexAxisValueFormatter {
    private String[] values;

    public XAxisValueFormatter(String[] values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value) {
        return values[(int) value];
    }
}

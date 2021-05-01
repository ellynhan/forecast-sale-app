package com.example.ready.Calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import androidx.annotation.NonNull;

public class AddTextToDates implements LineBackgroundSpan {
    private String test;

    public AddTextToDates(String text) {
        test = text;
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        paint.setColor(Color.RED);
        canvas.drawCircle(((left + right) / 2 - 20), (bottom + 15), 5, paint);
        canvas.drawText("10", ((left + right) / 2 - 10), (bottom + 25), paint);
    }
}

package com.example.ready.Calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import androidx.annotation.NonNull;

public class AddTextToDates implements LineBackgroundSpan {
    private String text;
    private int color;

    public AddTextToDates(String text, int color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        paint.setColor(this.color);
        canvas.drawCircle(((left + right) / 2 - 20), (bottom + 15), 5, paint);
        canvas.drawText(this.text, ((left + right) / 2 - 10), (bottom + 25), paint);
    }
}

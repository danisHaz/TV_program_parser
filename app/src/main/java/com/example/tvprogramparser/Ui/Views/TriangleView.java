package com.example.tvprogramparser.Ui.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.tvprogramparser.R;

public class TriangleView extends View {
    private Paint myPainter = new Paint();
    private Path myPath = new Path();

    Point point1 = new Point(0,
            (int)((Point.getY() - Point.convertDpToPx(60, getContext())) * 0.04));
    Point point2 = new Point((int)((Point.getX() - Point.convertDpToPx(150, getContext())) * 0.04),
            (int)((Point.getY() - Point.convertDpToPx(60, getContext())) * 0.04));
    Point point3 = new Point((int)((Point.getX() - Point.convertDpToPx(150, getContext())) * 0.04),
            0);

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        myPainter.setStyle(Paint.Style.FILL);
        myPainter.setColor(getResources().getColor(R.color.colorBackOne));
        myPainter.setAntiAlias(true);

        myPath.setFillType(Path.FillType.EVEN_ODD);
        Log.d("First", String.format("%d, %d", point1.x, point1.y));
        Log.d("Second", String.format("%d, %d", point2.x, point2.y));
        Log.d("Third", String.format("%d, %d", point3.x, point3.y));
        myPath.moveTo(point1.x, point1.y);
        myPath.lineTo(point2.x, point2.y);
        myPath.lineTo(point3.x, point3.y);
        myPath.lineTo(point1.x, point1.y);
        myPath.close();

        canvas.drawPath(myPath, myPainter);
    }
}

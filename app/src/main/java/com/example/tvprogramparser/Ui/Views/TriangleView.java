package com.example.tvprogramparser.Ui.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.ViewGroup;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.tvprogramparser.R;
import com.example.tvprogramparser.Ui.Activities.StartingActivity;

public class TriangleView extends View {
    private Paint myPainter = new Paint();
    private Path myPath = new Path();

    Point point1 = new Point(0,
            (int)((Point.getY() - Point.convertDpToPx(60, getContext())) * 0.04));
    public Point point2 = new Point((int)((Point.getX() - Point.convertDpToPx(150, getContext())) * 0.04),
            (int)((Point.getY() - Point.convertDpToPx(60, getContext())) * 0.04));
    Point point3 = new Point((int)((Point.getX() - Point.convertDpToPx(150, getContext())) * 0.04),
            0);

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMyLayoutParams(int widthInPx, int heightInPx) {
        this.getLayoutParams().width = widthInPx;
        this.getLayoutParams().height = heightInPx;
    }

//    @Override
//    protected void onMeasure(int a, int b) {
//        setMyLayoutParams(point2.x, point2.y);
//        super.onMeasure(a, b);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        myPainter.setStyle(Paint.Style.FILL);
        myPainter.setColor(getResources().getColor(R.color.colorBackOne));
        myPainter.setAntiAlias(true);

        myPath.setFillType(Path.FillType.EVEN_ODD);
        myPath.moveTo(point1.x, point1.y);
        myPath.lineTo(point2.x, point2.y);
        myPath.lineTo(point3.x, point3.y);
        myPath.lineTo(point1.x, point1.y);
        myPath.close();

        canvas.drawPath(myPath, myPainter);
    }
}

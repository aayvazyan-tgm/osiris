package at.pria.osiris.osiris.view.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author Ari Michael Ayvazyan
 * @version 05.04.2015
 */
public class EmulatorView extends View {
    Paint paint = new Paint();
    private final static int axis1Length = 10; // used for inverse kinematic and scaling
    private final static int axis1AngleCorrection = 180;
    private final static int axis2Length = 6;
    private final static int axis2AngleCorrection = 90;

    private int axis1ScaledLength;
    private int axis2ScaledLength;
    private Point axis1End;
    private Point axis2End;
    private int axis1Angle=0;
    private int axis2Angle=0;

    public EmulatorView(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                axis1Angle += 10;
                invalidate();
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        int baseX = w / 2;
        int baseY = 9 * (h / 10);
        Point baseEnd = new Point(baseX, baseY);

        //Scale the axes
        int maxAxisLength = (int) (((double) Math.min(w, h)) * (1d / 3d));
        int longestAxis = Math.max(axis1Length, axis2Length);
        if (axis1Length == longestAxis) {
            axis1ScaledLength = maxAxisLength;
            axis2ScaledLength = (axis1ScaledLength/axis1Length) * axis2Length;
        } else {
            axis2ScaledLength = maxAxisLength;
            axis1ScaledLength = (axis2ScaledLength/axis2Length) * axis1Length;
        }

        //Bottom
        canvas.drawLine(w / 10, baseY, 9 * (w / 10), baseY, paint);

        //Axes

        //Vertical
        {
            Point p2 = rotate(baseEnd, new Point(baseEnd.x, baseEnd.y + axis1ScaledLength), axis1Angle+axis1AngleCorrection);
            this.axis1End = p2;
            canvas.drawLine(baseEnd.x, baseEnd.y, p2.x, p2.y, paint);
        }

        //Horizontal
        {
            Point p1 = axis1End;
            Point p2 = new Point(p1.x, p1.y + axis2ScaledLength);
            p2 = this.axis2End = rotate(p1, p2, axis2Angle+axis2AngleCorrection);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
        }
    }

    /**
     * rotates a line around p1
     *
     * @param p1
     * @param p2
     * @param beta the axis1Angle
     * @return the new p2
     */
    private static Point rotate(Point p1, Point p2, double beta) {
        beta = beta * Math.PI / 180;

        int centerX = p1.x;
        int centerY = p1.y;
        double newX = centerX + (p2.x - centerX) * Math.cos(beta) - (p2.y - centerY) * Math.sin(beta);

        double newY = centerY + (p2.x - centerX) * Math.sin(beta) + (p2.y - centerY) * Math.cos(beta);
        return new Point((int) newX, (int) newY);
    }

    public void moveToAngle(int axis, int angle){
        switch (axis){
            case 1:
                axis1Angle=angle;
            case 2:
                axis2Angle=angle;
        }
    }

    public int getAngle(int axis){
        switch (axis){
            case 1:
                return axis1Angle;
            case 2:
                return axis2Angle;
        }
        return -1;
    }
}

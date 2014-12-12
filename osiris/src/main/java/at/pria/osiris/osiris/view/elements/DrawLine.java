package at.pria.osiris.osiris.view.elements;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import at.pria.osiris.osiris.R;

/**
 * Created by helmuthbrunner on 10/12/14.
 */
public class DrawLine extends Activity {

    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;

    public DrawLine() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_draw);

        imageView = (ImageView) this.findViewById(R.id.drawView);

        Display currentDisplay = getWindowManager().getDefaultDisplay();
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB_MR2) {
            //noinspection deprecation
            float dw= currentDisplay.getWidth();
            //noinspection deprecation
            float dh= currentDisplay.getHeight();

            bitmap = Bitmap.createBitmap((int) dw, (int) dh, Bitmap.Config.ARGB_8888);

        } else {
            Point p= new Point();
            currentDisplay.getSize(p);
            int dw= p.x;
            int dh= p.y;

            bitmap = Bitmap.createBitmap(dw, dh, Bitmap.Config.ARGB_8888);

        }

        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);

        imageView.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downx = event.getX();
                        downy = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        upx = event.getX();
                        upy = event.getY();
                        canvas.drawLine(downx, downy, upx, upy, paint);
                        imageView.invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

}

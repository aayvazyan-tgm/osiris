package at.pria.osiris.osiris.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import at.pria.osiris.osiris.R;

/**
 * Created by helmuthbrunner on 03/12/14.
 */
public class DrawFragment extends Fragment {

    private static DrawFragment INSTANCE;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private float downx = 0, downy = 0, upx = 0, upy = 0;

    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;

    public static DrawFragment getInstance(int sectionNumber) {

        if(INSTANCE==null) {

            DrawFragment fragment= new DrawFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }

        return INSTANCE;
    }

    public DrawFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, container, false);

        imageView = (ImageView) view.findViewById(R.id.drawView);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager manager = (WindowManager) imageView.getContext().getSystemService(Context.WINDOW_SERVICE);

        Display currentDisplay= manager.getDefaultDisplay();

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

        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action= event.getAction();
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

                return false;
            }

        });
    }

}

package at.pria.osiris.osiris.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import api.Points;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.util.AXCPWrapper;

/**
 * A fragment which allows the user to draw a line on the screen.
 *
 * Created by helmuthbrunner on 03/12/14.
 */
public class DrawFragment extends Fragment {

    private static DrawFragment INSTANCE;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private float startx=0, starty=0, endx=0, endy=0;

    private Button buttonClear, buttonSend;
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private ArrayList<Points> l;
    private ArrayList<ArrayList<Points>> allLists;

    /**
     * Creates a new DrawFragment instance
     * @param sectionNumber the sectionNumber from the fragments collection
     * @return the new instance
     */
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

    /**
     * Constructor
     */
    public DrawFragment() {
        allLists= new ArrayList<ArrayList<Points>>();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, container, false);

        // get view elements from the view
        imageView = (ImageView) view.findViewById(R.id.drawView);
        buttonClear= (Button) view.findViewById(R.id.buttonClear);
        buttonSend= (Button) view.findViewById(R.id.buttonSend);

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
        paint.setColor(Color.GREEN);    // sets the color of the line
        paint.setStrokeWidth(10.0f);    // sets the width of the line

        imageView.setImageBitmap(bitmap);

        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action= event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        startx=event.getX();
                        starty=event.getY();
                        l= new ArrayList<Points>();
                        allLists.add(l);

                        break;
                    case MotionEvent.ACTION_MOVE:

                        endx = event.getX();
                        endy = event.getY();

                        l.add(new Points((int) endx, (int) endy)); // add new Points to the list

                        canvas.drawLine(startx, starty, endx, endy, paint);
                        imageView.invalidate();

                        startx=endx;
                        starty=endy;

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }

        });

        // listener for the clear button
        // clears the list with the points
        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bitmap.eraseColor(Color.TRANSPARENT);
                imageView.invalidate();
                allLists.clear();
            }
        });

        // listener for the send button
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    AXCPWrapper.sendData(allLists);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}

package at.pria.osiris.osiris.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A class which provides a TableRow with two TextView-Components
 * Created by helmuthbrunner on 24/11/14.
 */
public class SensorRow extends TableRow {

    //Attributes
    private TextView sensorNameView, value;

    /**
     * Constructor
     * @param context the context from the root-view
     * @param name the name of the sensor
     */
    public SensorRow(Context context, String name) {
        super(context);

        super.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        sensorNameView = new TextView(context);
        sensorNameView.setText(name);
        sensorNameView.setGravity(Gravity.LEFT);
        sensorNameView.setTextColor(Color.BLACK);
        sensorNameView.setTypeface(sensorNameView.getTypeface(), Typeface.BOLD);

        super.addView(sensorNameView);

        value = new TextView(context);
        value.setGravity(Gravity.RIGHT);
        value.setTextColor(Color.BLACK);
        value.setTypeface(value.getTypeface(), Typeface.BOLD);

        super.addView(value);
    }

    /**
     * Method to set the new value
     * @param newValue the new value which will be displayed
     */
    public void updateValue(double newValue) {
        value.setText("" + newValue);
    }

}

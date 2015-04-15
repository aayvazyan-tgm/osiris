package at.pria.osiris.osiris.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

import java.util.concurrent.ConcurrentHashMap;

import at.pria.osiris.osiris.controllers.RobotArm;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.sensors.SensorRefreshable;
import at.pria.osiris.osiris.view.elements.SensorRow;

/**
 *
 * A fragment to which adds dynamic TableRows to the view
 *
 * Created by helmuthbrunner on 17/11/14.
 */
public class TableSensorValuesFragment extends Fragment implements SensorRefreshable {

    private static TableSensorValuesFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RobotArm robotArm;
    private Thread sensorRefresher;
    private TableLayout table;
    final private ConcurrentHashMap<String, SensorRow> sensorMap;

    private int count;

    /**
     * Returns a instance from this class
     * @param sectionNumber the sectionnumber
     * @return a instance from this class
     */
    public static TableSensorValuesFragment getInstance(int sectionNumber, @NonNull RobotArm robotArm) {
        if (INSTANCE == null) {
            TableSensorValuesFragment fragment = new TableSensorValuesFragment();
            fragment.robotArm = robotArm;

            //save the args in the Bundle
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE = fragment;
        } else {
            INSTANCE.robotArm = robotArm;
        }
        return INSTANCE;
    }

    /**
     * Constructor
     */
    public TableSensorValuesFragment() {
        // Required empty public constructor
        sensorMap= new ConcurrentHashMap<String, SensorRow>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_values, container, false);

        // removes all views on the table
        if(table!=null) {
            table.removeAllViews();
        }

        table= (TableLayout) view.findViewById(R.id.tableLayout);

        for (SensorRow sensorRow : sensorMap.values()) {
            table.addView(sensorRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void refresh(final double newValue, final String sensorName) {
        final Activity activity = getActivity();
        Log.e("OsirisSensorValueReceived","newVal: "+newValue);
        if(activity == null)
            return;

        final SensorRow sr= sensorMap.get(sensorName);

        // if null new row will be created
        if(sr == null) {

            SensorRow sr3= new SensorRow(getActivity(), sensorName);
            sensorMap.put(sensorName, sr3);

            final SensorRow sr2= sr3;
            activity.runOnUiThread(new Runnable() {

                public void run() {
                    table.removeView(sr2);
                    table.addView(sr2, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    sr2.updateValue(newValue);
                }

            });

        }else {
            activity.runOnUiThread(new Runnable() {

                public void run() {
                    sr.updateValue(newValue);
                }

            });
        }
    }
}

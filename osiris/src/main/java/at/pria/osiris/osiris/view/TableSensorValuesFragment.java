package at.pria.osiris.osiris.view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.network.RemoteRobotarm;
import at.pria.osiris.osiris.sensors.SensorRefreshable;
import at.pria.osiris.osiris.sensors.SensorRefresher;
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
    private Thread sensorRefresher;
    private TableLayout table;
    final private ConcurrentHashMap<String, SensorRow> sensorMap;
    private SensorRow sr;

    private OnFragmentInteractionListener mListener;

    /**
     * Returns a instance from this class
     * @param sectionNumber the sectionnumber
     * @return a instance from this class
     */
    public static TableSensorValuesFragment getInstance(int sectionNumber) {

        if (INSTANCE == null) {
            TableSensorValuesFragment fragment = new TableSensorValuesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            try {
                fragment.sensorRefresher = new Thread(new SensorRefresher(RemoteRobotarm.getInstance(), fragment));
                fragment.sensorRefresher.start();
                Log.d("OSIRIS_DEBUG_MESSAGES", "Thread started");
            } catch (IOException e) {
                e.printStackTrace();
            }

            INSTANCE = fragment;
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
        table= (TableLayout) view.findViewById(R.id.tableLayout);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void refresh(final double newValue, final String sensorName) {
        final Activity activity = getActivity();

        if(activity == null)
            return;

        sr= sensorMap.get(sensorName);
        // if null new row will be created
        Log.e("Osiris", "SensorName" + sensorName);
        Log.e("Osiris", "MapSize: " + sensorMap.size());

        if(sr == null) {

            activity.runOnUiThread(new Runnable() {

                public void run() {
                    sr= new SensorRow(getActivity(), sensorName);
                    table.addView(sr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    sr.updateValue(newValue);
                    sensorMap.put(sensorName, sr);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}

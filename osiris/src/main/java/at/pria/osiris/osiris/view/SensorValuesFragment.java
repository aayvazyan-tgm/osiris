package at.pria.osiris.osiris.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.network.RemoteRobotarm;
import at.pria.osiris.osiris.sensors.SensorRefreshable;
import at.pria.osiris.osiris.sensors.SensorRefresher;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorValuesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorValuesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorValuesFragment extends Fragment implements SensorRefreshable{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static SensorValuesFragment INSTANCE;
    private Thread sensorRefresher;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorValuesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorValuesFragment newInstance(String param1, String param2) {
        SensorValuesFragment fragment = new SensorValuesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        try {
            fragment.sensorRefresher=new Thread(new SensorRefresher(RemoteRobotarm.getInstance(),fragment));
            fragment.sensorRefresher.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SensorValuesFragment getInstance(int sectionNumber) {
        if (INSTANCE == null) {
            SensorValuesFragment fragment = new SensorValuesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE = fragment;
        }
        return INSTANCE;
    }

    public SensorValuesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensor_values, container, false);
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
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();



        mListener = null;
    }

    @Override
    public void refresh(double newValue) {
        final TextView textView = (TextView) getView().findViewById(R.id.sensor01Value);
        textView.setText(""+ newValue);
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

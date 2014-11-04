package at.pria.osiris.osiris;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import at.pria.osiris.osiris.api.Axis;
import at.pria.osiris.osiris.network.RemoteRobotarm;
import at.pria.osiris.osiris.network.RoboArmConfig;
import at.pria.osiris.osiris.util.EnumUtil;

import java.io.IOException;

/**
 * @author Ari Michael Ayvazyan
 * @version 29.10.2014
 */
public class ControllerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ControllerFragment INSTANCE;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ControllerFragment getInstance(int sectionNumber) {
        if (INSTANCE == null) {
            ControllerFragment fragment = new ControllerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE = fragment;
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        //Button actionListeners
        final Button buttonPositivePower = (Button) rootView.findViewById(R.id.positivePower);
        buttonPositivePower.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    doPower(v,true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopPower(v);
                }
                return false;
            }
        });
        final Button buttonNegativePower = (Button) rootView.findViewById(R.id.negativePower);
        buttonNegativePower.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    doPower(v,false);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopPower(v);
                }
                return false;
            }
        });
        //Axis selection
        Axis.values();
        final Spinner motorSpinner = (Spinner) rootView.findViewById(R.id.motorSelectionSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, EnumUtil.getEnumNames(Axis.class));
        motorSpinner.setAdapter(spinnerArrayAdapter);
        motorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                RoboArmConfig.getInstance().setSelectedAxis(selectedItem);
                Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Warning: No axis selected", Toast.LENGTH_SHORT).show();
            }
        });
        //ProgressBar
        final SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                RoboArmConfig.getInstance().setPercentPower(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(), "Power at :" +seekBar.getProgress()+"%!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    public void stopPower(View view){
        try {
            RoboArmConfig cfg = RoboArmConfig.getInstance();
            RemoteRobotarm.getInstance().stopAxis(RoboArmConfig.getInstance().getSelectedAxis());
        } catch (IOException e) {
            Toast.makeText(view.getContext(), "Not connected", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(view.getContext(), "Stop the engines!", Toast.LENGTH_SHORT).show();
    }
    public void doPower(View view,boolean positive) {
        int direction=1;
        if(!positive)direction=-1;
        try {
            RoboArmConfig cfg = RoboArmConfig.getInstance();
            RemoteRobotarm.getInstance().turnAxis(RoboArmConfig.getInstance().getSelectedAxis(),
                    direction*
                            ((int)(
                                (double)RemoteRobotarm.MAX_POWER *
                                ((double)cfg.getPercentPower()/100d)
                            ))
            );
        } catch (IOException e) {
            Toast.makeText(view.getContext(), "Not connected", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(view.getContext(), "Pew Pew", Toast.LENGTH_SHORT).show();
    }
}

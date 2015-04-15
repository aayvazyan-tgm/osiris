package at.pria.osiris.osiris.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import api.Axis;
import at.pria.osiris.osiris.controllers.RobotArm;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.util.RoboArmConfig;
import at.pria.osiris.osiris.util.EnumUtil;
import at.pria.osiris.osiris.view.elements.ButtonVisualiser;
import at.pria.osiris.osiris.view.elements.EmulatorView;

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
    private ButtonVisualiser buttonPositivePowerVisualiser;
    private ButtonVisualiser buttonNegativePowerVisualiser;
    private Controller robotController;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ControllerFragment getInstance(int sectionNumber, @NonNull Controller robotController) {

        if(robotController==null)
            throw new RuntimeException("robotController may not be null.");

        if (INSTANCE == null) {
            ControllerFragment fragment = new ControllerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.robotController=robotController;

            INSTANCE = fragment;
        }
        INSTANCE.robotController=robotController;
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        //
        //final Activity activity= getActivity();
        //
        //WifiManager wifi = (WifiManager) activity.getSystemService(activity.WIFI_SERVICE);
        //if (wifi.isWifiEnabled()){
        //    Log.d("Osisir", "Wifi is enabled");
        //} else {
        //    wifi.setWifiEnabled(true);
        //}

        //KeyListeners
        View inputListener = rootView.findViewById(R.id.input_fetcher);
        inputListener.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RobotArm remoteRobotArm;
                try {
                    remoteRobotArm = robotController.getRobotArm();
                } catch (ConnectionNotEstablishedException e) {
                    Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Log.d("Osiris", "----- KeyCode: "+ keyCode + " Event: " +  event);
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        //DPAD
                        case KeyEvent.KEYCODE_DPAD_UP:
                            break;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            break;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            break;
//                    //ARROWS
//                        case KeyEvent.:
//                            break;
//                    case KeyEvent.:
//                        break;
//                    case KeyEvent.:
//                        break;
//                    case KeyEvent.:
//                        break;
                        //RightButtons
                        case KeyEvent.KEYCODE_BUTTON_A:
                            break;
                        case KeyEvent.KEYCODE_BUTTON_B:
                            break;
                        case KeyEvent.KEYCODE_BUTTON_X:
                            break;
                        case KeyEvent.KEYCODE_BUTTON_Y:
                            break;
                        //Backside Buttons
                        case KeyEvent.KEYCODE_BUTTON_L1:
                            remoteRobotArm.turnAxis(0,100);
                        break;
                        case KeyEvent.KEYCODE_BUTTON_R1:
                            remoteRobotArm.turnAxis(0,-100);
                        break;
                        case KeyEvent.KEYCODE_BUTTON_L2:
                            remoteRobotArm.turnAxis(1,100);
                        break;
                        case KeyEvent.KEYCODE_BUTTON_R2:
                            remoteRobotArm.turnAxis(1,-100);
                        break;
                    }
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BUTTON_L1:
                        case KeyEvent.KEYCODE_BUTTON_R1:
                            remoteRobotArm.stopAxis(0);
                            break;
                        case KeyEvent.KEYCODE_BUTTON_L2:
                        case KeyEvent.KEYCODE_BUTTON_R2:
                            remoteRobotArm.stopAxis(1);
                            break;
                    }
                }
                return true;
            }
        });

        //Buttons
        //
        //Positive Power
        final Button buttonPositivePower = (Button) rootView.findViewById(R.id.positivePower);
        this.buttonPositivePowerVisualiser = new ButtonVisualiser(buttonPositivePower);
        buttonPositivePower.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    doPower(v, true, buttonPositivePowerVisualiser);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopPower(v, buttonPositivePowerVisualiser);
                }
                return false;
            }
        });

        //Negative Power
        final Button buttonNegativePower = (Button) rootView.findViewById(R.id.negativePower);
        this.buttonNegativePowerVisualiser = new ButtonVisualiser(buttonNegativePower);
        buttonNegativePower.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    doPower(v, false, buttonNegativePowerVisualiser);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopPower(v, buttonNegativePowerVisualiser);
                }
                return false;
            }
        });

        //Axis selection
        final Spinner motorSpinner = (Spinner) rootView.findViewById(R.id.motorSelectionSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, EnumUtil.getEnumNames(Axis.class));
        motorSpinner.setAdapter(spinnerArrayAdapter);
        motorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedItem = i; //This requires the list to be sorted from servo 0 to the last one available
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
                Toast.makeText(getActivity(), "Power at :" + seekBar.getProgress() + "%!", Toast.LENGTH_SHORT).show();
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

    public void stopPower(View view, ButtonVisualiser triggeringButton) {
        triggeringButton.stopVisualise();
        stopPower(view);
    }

    public void stopPower(View view) {
        try {
            RoboArmConfig cfg = RoboArmConfig.getInstance();
            //Emulator
            RobotArm emulator = EmulatorView.getInstance(view.getContext());
            emulator.stopAxis(cfg.getSelectedAxis());
            //Real RobotArm
            RobotArm robotArm = robotController.getRobotArm();

            robotArm.stopAxis(cfg.getSelectedAxis());

        } catch (ConnectionNotEstablishedException e) {
            Toast.makeText(view.getContext(), "Not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void doPower(View view, boolean positive, ButtonVisualiser triggeringButton) {
        triggeringButton.startVisualise();
        doPower(view, positive);
    }

    public void doPower(View view, boolean positive) {
        int direction = 1;
        if (!positive) direction = -1;
        try {
            RoboArmConfig cfg = RoboArmConfig.getInstance();

            //Emulator
            RobotArm emulator = EmulatorView.getInstance(view.getContext());
            emulator.turnAxis(cfg.getSelectedAxis(),
                    direction * ((int) (100 * ((double) cfg.getPercentPower() / 100d)))
            );

            //Real robotArm
            RobotArm robotArm = robotController.getRobotArm();

            robotArm.turnAxis(cfg.getSelectedAxis(),
                    direction * ((int) (100 * ((double) cfg.getPercentPower() / 100d)))
            );
        } catch (ConnectionNotEstablishedException e) {
            Toast.makeText(view.getContext(), "Not connected", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(view.getContext(), "Could not perform action", Toast.LENGTH_SHORT).show();
        }
    }
}

package at.pria.osiris.osiris.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import api.Axis;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.network.RemoteRobotarm;
import at.pria.osiris.osiris.network.RoboArmConfig;
import at.pria.osiris.osiris.util.EnumUtil;
import at.pria.osiris.osiris.view.elements.ButtonVisualiser;

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
    private ButtonVisualiser buttonPositivePowerVisualiser;
    private ButtonVisualiser buttonNegativePowerVisualiser;
    private EditText xValue;
    private EditText yValue;
    private EditText zValue;

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

        //KeyListeners
        View inputListener = rootView.findViewById(R.id.input_fetcher);
        inputListener.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RemoteRobotarm remoteRobotarm;
                try {
                    remoteRobotarm = RemoteRobotarm.getInstance();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
                    return true;
                }
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
//                    case KeyEvent.:
//                        break;
//                    case KeyEvent.:
//                        break;
//                    case KeyEvent.:
//                        break;
//                    case KeyEvent.:
//                        break;
                        //RightButtons
//                    case KeyEvent.Butto:
//                        break;
//                    case KeyEvent.:
//                        break;
//                    case KeyEvent.:
//                        break;
//                    case KeyEvent.:
//                        break;
                        //Backside Buttons
                        case KeyEvent.KEYCODE_BUTTON_L1:
                            remoteRobotarm.turnAxis(Axis.BASE,100);
                        break;
                        case KeyEvent.KEYCODE_BUTTON_R1:
                            remoteRobotarm.turnAxis(Axis.BASE,-100);
                        break;
                        case KeyEvent.KEYCODE_BUTTON_L2:
                            remoteRobotarm.turnAxis(Axis.AXISTWO,100);
                        break;
                        case KeyEvent.KEYCODE_BUTTON_R2:
                            remoteRobotarm.turnAxis(Axis.AXISTWO,-100);
                        break;
                    }
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    remoteRobotarm.stopAll();
                }
                return true;
            }
        });

        //TextFields
        //
        //GoToPosition
        xValue = (EditText) rootView.findViewById(R.id.xValue);
        yValue = (EditText) rootView.findViewById(R.id.yValue);
        zValue = (EditText) rootView.findViewById(R.id.zValue);

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

        //GoToPosition
        final Button goToPosition = (Button) rootView.findViewById(R.id.goToPositionButton);
        goToPosition.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    double xValueActual = Double.parseDouble(xValue.getText().toString());
                    double yValueActual = Double.parseDouble(yValue.getText().toString());
                    double zValueActual = Double.parseDouble(zValue.getText().toString());
                    try {
                        RemoteRobotarm.getInstance().moveTo(xValueActual, yValueActual, zValueActual);
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
                    }
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
            RemoteRobotarm robotArm = RemoteRobotarm.getInstance();

            robotArm.stopAxis(cfg.getSelectedAxis());

        } catch (IOException e) {
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
            RemoteRobotarm robotArm = RemoteRobotarm.getInstance();

            robotArm.turnAxis(cfg.getSelectedAxis(),
                    direction *
                            ((int) (
                                    (double) RemoteRobotarm.MAX_POWER *
                                            ((double) cfg.getPercentPower() / 100d)
                            ))
            );
        } catch (IOException e) {
            Toast.makeText(view.getContext(), "Not connected", Toast.LENGTH_SHORT).show();
        }
    }
}

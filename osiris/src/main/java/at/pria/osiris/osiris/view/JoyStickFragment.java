package at.pria.osiris.osiris.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import api.Axis;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.util.EnumUtil;
import at.pria.osiris.osiris.util.RoboArmConfig;
import at.pria.osiris.osiris.view.elements.joystick.*;

/**
 * Created by helmuthbrunner on 26/02/15.
 */
public class JoyStickFragment extends Fragment {

    private static final String TAG= "JOYSTICK";

    private static JoyStickFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView txtX1, txtY1, txtX2, txtY2;
    private DualJoystickView joystick;
    private Spinner spinner_left, spinner_right;

    public static JoyStickFragment getInstance(int sectionNumber) {

        if(INSTANCE==null) {

            JoyStickFragment fragment= new JoyStickFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            Log.d(TAG, "New Fragment Joystick");

            INSTANCE= fragment;
        }

        return INSTANCE;

    }

    public JoyStickFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.dualjoystick, container, false);

        txtX1= (TextView) rootView.findViewById(R.id.TextViewX1);
        txtY1= (TextView) rootView.findViewById(R.id.TextViewY1);
        txtX2= (TextView) rootView.findViewById(R.id.TextViewX2);
        txtY2= (TextView) rootView.findViewById(R.id.TextViewY2);

        txtX1.setVisibility(View.INVISIBLE);
        txtY1.setVisibility(View.INVISIBLE);
        txtX2.setVisibility(View.INVISIBLE);
        txtY2.setVisibility(View.INVISIBLE);

        joystick= (DualJoystickView) rootView.findViewById(R.id.dualjoystickView);
        joystick.setOnJostickMovedListener(_listenerLeft, _listenerRight);

        joystick.invalidate();

        spinner_left= (Spinner) rootView.findViewById(R.id.spinner_left);
        ArrayAdapter<String> spinnerArrayAdapter_left = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, EnumUtil.getEnumNames(Axis.class));
        spinner_left.setAdapter(spinnerArrayAdapter_left);
        spinner_left.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedItem = i;
                RoboArmConfig.getInstance().setSelectedAxis(selectedItem);
                Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Warning: No axis selected", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_right= (Spinner) rootView.findViewById(R.id.spinner_right);
        ArrayAdapter<String> spinnerArrayAdapter_right = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, EnumUtil.getEnumNames(Axis.class));
        spinner_right.setAdapter(spinnerArrayAdapter_right);
        spinner_right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedItem = i;
                RoboArmConfig.getInstance().setSelectedAxis(selectedItem);
                Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Warning: No axis selected", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private JoystickMovedListener _listenerLeft = new JoystickMovedListener() {
        @Override
        public void OnMoved(int pan, int tilt) {
            txtX1.setText(Integer.toString(pan));
            txtY1.setText(Integer.toString(tilt));
        }

        @Override
        public void OnReleased() {
            txtX1.setText("released");
            txtY1.setText("released");
        }

        public void OnReturnedToCenter() {
            txtX1.setText("stopped");
            txtY1.setText("stopped");
        };
    };

    private JoystickMovedListener _listenerRight = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {
            txtX2.setText(Integer.toString(pan));
            txtY2.setText(Integer.toString(tilt));
        }

        @Override
        public void OnReleased() {
            txtX2.setText("released");
            txtY2.setText("released");
        }

        public void OnReturnedToCenter() {
            txtX2.setText("stopped");
            txtY2.setText("stopped");
        };
    };
}

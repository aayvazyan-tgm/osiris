package at.pria.osiris.osiris;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
public class ControllerFragment extends Fragment implements View.OnClickListener {
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
        View rootView = inflater.inflate(R.layout.fragment_controll, container, false);
        //Button actionListeners
        final Button buttonPositivePower = (Button) rootView.findViewById(R.id.positivePower);
        buttonPositivePower.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                positivePower(v);
            }
        });
        final Button buttonNegativePower = (Button) rootView.findViewById(R.id.negativePower);
        buttonNegativePower.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                negativePower(v);
            }
        });
        //Axis selection
        Axis.values();
        final Spinner motorSpinner = (Spinner) rootView.findViewById(R.id.motorSelectionSpinner);
        ArrayAdapter<String> spinnerArrayAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, EnumUtil.getEnumNames(Axis.class));
        motorSpinner.setAdapter(spinnerArrayAdapter);
        motorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem=(String)adapterView.getItemAtPosition(i);
                RoboArmConfig.getInstance().setSelectedAxis(selectedItem);
                Toast.makeText(getActivity(), "Selected: "+selectedItem, Toast.LENGTH_SHORT).show();
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
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void positivePower(View view) {
        try {
            RemoteRobotarm.getInstance().turnAxis(RoboArmConfig.getInstance().getSelectedAxis(), 100, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Pew Pew", Toast.LENGTH_SHORT).show();
    }

    public void negativePower(View view) {
        try {
            RemoteRobotarm.getInstance().turnAxis(RoboArmConfig.getInstance().getSelectedAxis(), -100, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Pow Pow", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        final Button buttonPositivePower = (Button) getActivity().findViewById(R.id.positivePower);
        buttonPositivePower.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                positivePower(v);
            }
        });
        final Button buttonNegativePower = (Button) getActivity().findViewById(R.id.negativePower);
        buttonNegativePower.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                negativePower(v);
            }
        });
    }
}

package at.pria.osiris.osiris;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

    public ControllerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_controll, container, false);
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
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void positivePower(View view) {
        Toast.makeText(getActivity(), "Pew Pew", Toast.LENGTH_SHORT).show();
    }

    public void negativePower(View view) {
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

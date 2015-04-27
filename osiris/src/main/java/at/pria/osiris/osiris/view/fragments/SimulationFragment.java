package at.pria.osiris.osiris.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import at.pria.osiris.OsirisSimulation;
import at.pria.osiris.osiris.MainActivity;

/**
 * A Fragement to Display the libgdx simulationx
 *
 * Created by helmuthbrunner on 25/04/15.
 */
public class SimulationFragment extends AndroidFragmentApplication {

    private static SimulationFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private OsirisSimulation osirisSimulation;
    private AndroidApplicationConfiguration configuration;

    public static SimulationFragment getInstance(int sectionNumber) {

        if(INSTANCE==null) {

            SimulationFragment fragment= new SimulationFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }
        return INSTANCE;
    }

    public SimulationFragment() {
        configuration= new AndroidApplicationConfiguration();
        osirisSimulation= new OsirisSimulation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initializeForView(osirisSimulation, configuration);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

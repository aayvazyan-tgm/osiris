package at.pria.osiris.osiris.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.util.Storeage;

/**
 * A fragment to controll the user settings in the app.
 *
 * Does nothing
 *
 * Created by helmuthbrunner on 16/02/15.
 */
public class SettingsFragment extends Fragment {

    private static SettingsFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static SettingsFragment getInstance(int sectionNumber) {

        if(INSTANCE==null) {

            SettingsFragment fragment= new SettingsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }

        return INSTANCE;

    }

    public SettingsFragment() {
        Storeage storeage= Storeage.getInstance();
        Log.d("Test", "" + storeage.getRobotController());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        return rootView;
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

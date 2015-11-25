package at.pria.osiris.osiris.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.Controller;

/**
 * A fragment which displays all the profiles from the database.
 * <p/>
 * If you press long on the the item two buttons will appeare. One edit- and one deletebutton.
 * <p/>
 * Created by helmuthbrunner on 10/02/15.
 */
public class Teaching extends Fragment {

    private static Teaching INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Controller robotController;

    private final static String TAG = "Osiris";


    /**
     * Creates a new ProfileFragment instance
     *
     * @param sectionNumber the sectionNumber from the fragments collection
     * @return the new instance
     */
    public static Teaching getInstance(int sectionNumber, Controller robot) {

        if (INSTANCE == null) {

            Teaching fragment = new Teaching();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE = fragment;
        }
        INSTANCE.robotController = robot;

        return INSTANCE;
    }

    public Teaching() {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teaching, container, false);
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

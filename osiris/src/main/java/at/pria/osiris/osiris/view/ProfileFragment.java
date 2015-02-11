package at.pria.osiris.osiris.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;

import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.ControllerType;
import at.pria.osiris.osiris.util.DataBaseHandler;
import at.pria.osiris.osiris.view.elements.Profile;

import java.util.List;

/**
 * A fragment which displays all the profiles from the database
 *
 * mean while for debug only
 *
 * Created by helmuthbrunner on 10/02/15.
 */
public class ProfileFragment extends Fragment {

    private static ProfileFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Creates a new DrawFragment instance
     * @param sectionNumber the sectionNumber from the fragments collection
     * @return the new instance
     */
    public static ProfileFragment getInstance(int sectionNumber) {

        if(INSTANCE==null) {

            ProfileFragment fragment= new ProfileFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }

        return INSTANCE;
    }

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Activity activity = getActivity();

        DataBaseHandler db= new DataBaseHandler(activity);

        // debug data
        db.addProfile(new Profile(0, "192.168.0.2", 8889, ControllerType.Botball));
        db.addProfile(new Profile(1, "192.168.0.3", 8289, ControllerType.Hedgehog));
        db.addProfile(new Profile(2, "192.168.0.4", 8839, ControllerType.Botball));

        List<Profile> list= db.getAll();

        for(Profile p : list) {
            Log.d("Osiris", p.toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

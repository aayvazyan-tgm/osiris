package at.pria.osiris.osiris.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.ControllerType;
import at.pria.osiris.osiris.util.DataBaseHandler;
import at.pria.osiris.osiris.view.elements.Profile;

import java.util.List;

/**
 * A fragment which displays all the profiles from the database
 *
 * Created by helmuthbrunner on 10/02/15.
 */
public class ProfileFragment extends Fragment {

    private static ProfileFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private SimpleCursorAdapter sca;
    private Cursor cursor;

    private int[] to;
    private String[] columns;

    private ListView listView;
    private DataBaseHandler db;

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

        columns = new String[] {
                DataBaseHandler._ID,
                DataBaseHandler.KEY_HOST,
                DataBaseHandler.KEY_PORT,
                DataBaseHandler.KEY_TYPE
        }; // Define the tables for the cursor

        to= new int[] {
                R.id.id,
                R.id.host,
                R.id.port,
                R.id.type
        }; // Define the output TextViews from the profiles_view.xml file

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        cursor= new DataBaseHandler(getActivity()).fetchAllProfiles();

        listView= (ListView) rootView.findViewById(R.id.listViewProfile);

        final Activity activity = getActivity();

        sca= new SimpleCursorAdapter(activity.getBaseContext(), R.layout.profiles_view, cursor, columns, to, 0);

        this.displayListView();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = getActivity();
        db= new DataBaseHandler(activity);

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

    /**
     * add the views to the listView
     */
    private void displayListView() {

        listView.setAdapter(sca);

        final Activity activity = getActivity();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView,
                                    View view,
                                    int position,
                                    long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                Toast.makeText(activity.getBaseContext(),
                        countryCode,
                        Toast.LENGTH_SHORT).show();

            }
        });

    }
}

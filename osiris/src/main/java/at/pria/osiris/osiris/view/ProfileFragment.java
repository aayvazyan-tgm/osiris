package at.pria.osiris.osiris.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;

import android.widget.*;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
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

    private View current_delete_button, current_edit_button;

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

        //debug data
        //db.addProfile(new Profile(0, "192.168.0.2", 8889, ControllerType.Botball));
        //db.addProfile(new Profile(1, "192.168.0.3", 8289, ControllerType.Hedgehog));
        //db.addProfile(new Profile(2, "192.168.0.4", 8839, ControllerType.Botball));
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final long idd= id;
                current_delete_button= view.findViewById(R.id.delete_button);
                current_delete_button.setVisibility(View.VISIBLE);

                current_delete_button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Activity activity = getActivity();
                        db= new DataBaseHandler(activity);

                        db.delete(idd); // delete the item over the id

                        // refresh the GUI
                        listView.setAdapter(newAdapter());
                    }

                });

                current_edit_button= view.findViewById(R.id.edit_button);
                current_edit_button.setVisibility(View.VISIBLE);

                current_edit_button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Activity activity = getActivity();
                        DataBaseHandler db= new DataBaseHandler(activity);
                        Profile p= db.getProfile(idd);
                        Log.d("Osiris", "Hostname" + p.getHost());
                        Log.d("Osiris", "Port" + p.getPort());
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, NewProfileFragment.getInstance(5, p)).commit();

                    }

                });

                return false;
            }

        });

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

    /**
     * Method to update the content in the ListView
     * @return a new ListAdapter
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ListAdapter newAdapter() {
        final Activity activity = getActivity();
        cursor= new DataBaseHandler(activity).fetchAllProfiles();
        return new SimpleCursorAdapter(activity.getBaseContext(), R.layout.profiles_view, cursor, columns, to, 0);
    }

}

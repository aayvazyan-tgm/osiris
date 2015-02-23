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
 * A fragment which displays all the profiles from the database.
 *
 * If you press long on the the item two buttons will appeare. One edit- and one deletebutton.
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
        // set the parameters to the adapter
        sca= new SimpleCursorAdapter(activity.getBaseContext(), R.layout.profiles_view, cursor, columns, to, 0);
        this.displayListView();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = getActivity();
        db= new DataBaseHandler(activity);

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

        // setting a listener to the listview
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // checks if some buttons are visible.
                if(current_delete_button != null) {
                    if(current_delete_button.getVisibility() == View.VISIBLE) {
                        current_delete_button.setVisibility(View.INVISIBLE);
                        current_edit_button.setVisibility(View.INVISIBLE);
                    }
                }

                final long idd= id;
                current_delete_button= view.findViewById(R.id.delete_button);
                current_delete_button.setVisibility(View.VISIBLE);

                // setting a listener to the delete button
                current_delete_button.setOnClickListener(new View.OnClickListener() {

                    /**
                     * Removes the listView element and the dataset in the database.
                     * @param v
                     */
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

                // setting a listener to the edit button
                current_edit_button.setOnClickListener(new View.OnClickListener() {

                    /**
                     * Forwards to the NewFragmentProfile and fills in the data from the current profile
                     * @param v
                     */
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

        // set a listener to the listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * This method makes a toast with the id, of the clicked row.
             *
             * @param listView
             * @param view
             * @param position
             * @param id
             */
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
     * Method to update the content in the ListView if one of the values has changed.
     * @return a new ListAdapter
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ListAdapter newAdapter() {
        final Activity activity = getActivity();
        cursor= new DataBaseHandler(activity).fetchAllProfiles();
        return new SimpleCursorAdapter(activity.getBaseContext(), R.layout.profiles_view, cursor, columns, to, 0);
    }

}

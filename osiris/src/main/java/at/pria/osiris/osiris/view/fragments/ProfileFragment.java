package at.pria.osiris.osiris.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
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
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.orm.DBQuery;
import at.pria.osiris.osiris.orm.ProfileORM;
import at.pria.osiris.osiris.util.Storeage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.software.shell.fab.*;

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
    private Controller robotController;

    private final static String TAG= "Osiris";

    private View current_delete_button, current_edit_button;
    private ActionButton actionButton;

    /**
     * Creates a new ProfileFragment instance
     * @param sectionNumber the sectionNumber from the fragments collection
     * @return the new instance
     */
    public static ProfileFragment getInstance(int sectionNumber, Controller robot) {

        if(INSTANCE==null) {

            ProfileFragment fragment= new ProfileFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }
        INSTANCE.robotController=robot;

        return INSTANCE;
    }

    public ProfileFragment() {

        columns = new String[] {
                "_id",
                "hostname",
                "port",
                "controller_type"
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

        actionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        actionButton.setImageResource(R.drawable.fab_plus_icon);
        //actionButton.setButtonColor(Color.RED); // red button


        Resources res = getResources();
        int color= res.getColor(R.color.background_menu);
        actionButton.setButtonColor(color); // button color

        actionButton.setShadowXOffset(3.5f);
        actionButton.setShadowYOffset(3.5f);

        actionButton.show();
        actionButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d(TAG, "Plus Button Pressed");

                // jump to the NewProfileFragment
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, NewProfileFragment.getInstance(5, null, robotController)).commit();
            }
        });

        Log.d(TAG, "ActionButtonState: " + actionButton.getState());

        try {
            cursor= DBQuery.getCursor(getActivity().getBaseContext());
            Log.d("Osiris", "New Cursor Created");
        } catch (SQLException e) {
            Log.d("Osiris", "Expection by Cursor", e);
        }

        listView= (ListView) rootView.findViewById(R.id.listViewProfile);

        final Activity activity = getActivity();

        // debug only
        String erg= "";
        for(int i=0; i<cursor.getColumnNames().length;i++) {
            erg += cursor.getColumnNames()[i] + "\n";
        }
        Log.d("Osiris", erg);

        // set the parameters to the adapter
        sca= new SimpleCursorAdapter(activity.getBaseContext(), R.layout.profiles_view, cursor, columns, to, 0);
        this.displayListView();

        List<ProfileORM> items= new ArrayList<>();

        try {
            items= DBQuery.getAll(activity);
        } catch (SQLException e) {
            Log.d("Osiris", "SQL-Exception", e);
        }

        for(ProfileORM o : items) {
            Log.d("Osiris", "\n From ORM \n" + o.toString());
        }

        // refresh the GUI
        listView.setAdapter(newAdapter());

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

                // checks if some buttons are visible
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

                        // delete over the id
                        try {
                            DBQuery.deleteOverId(activity.getBaseContext(), String.valueOf(idd) );
                        } catch (SQLException e) {
                            Log.d("Osiris", "Exception in deleteOverId", e);
                        }

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

                        ProfileORM p= null;
                        try {
                            // select the selected Element
                            p= DBQuery.getStoredPackages(activity, String.valueOf(idd) );
                        } catch (SQLException e) {
                            Log.d("Osiris", "Exception", e);
                        }

                        // jump to the NewProfileFragment
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, NewProfileFragment.getInstance(5, p, robotController)).commit();
                    }

                });
                return false;
            }

        });

        // set a listener to the listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * This method makes a toast with the id, of the clicked row.
             */
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                ProfileORM po= null;

                try {
                    po= DBQuery.getStoredPackages(activity.getBaseContext(), String.valueOf(id));
                } catch (SQLException e) {
                    Log.d(TAG, "SQLException in onItemClick", e);
                }

                // Not Tested -----

                /**
                 * - "hedgehog"
                 - "hedgehogdirect"
                 - "botball"
                 */

                // Hedgehog Controller
                try {
                    robotController.getSetup().setup(robotController.getRobotArm());
                    Storeage storeage= Storeage.getInstance();
                    storeage.setRobotController(robotController);
                } catch (ConnectionNotEstablishedException e) {
                    e.printStackTrace(); // No exception will be thrown
                    Log.d(TAG, "Not connected Exception", e);
                    Toast.makeText(activity.getBaseContext(), "Connection not yet Established", Toast.LENGTH_SHORT).show();
                }
            }

                // Get the id from this row in the database
                //String selected_id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                //Toast.makeText(activity.getBaseContext(),
                //        selected_id,
                //        Toast.LENGTH_SHORT).show();
        });

    }

    /**
     * Method to update the content in the ListView if one of the values has changed.
     * @return a new ListAdapter
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ListAdapter newAdapter() {
        final Activity activity = getActivity();
        try {
            cursor= DBQuery.getCursor(activity.getBaseContext());
        } catch (SQLException e) {
            Log.d("Osiris", "Exception in method new Adapter", e);
        }
        return new SimpleCursorAdapter(activity.getBaseContext(), R.layout.profiles_view, cursor, columns, to, 0);
    }

}

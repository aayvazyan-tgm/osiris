package at.pria.osiris.osiris.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import api.Axis;

import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.orm.DBQuery;
import at.pria.osiris.osiris.orm.ProfileORM;

import java.sql.SQLException;

/**
 * A fragement to create a new profile or edit a profile
 *
 * Created by helmuthbrunner on 16/02/15.
 */
public class NewProfileFragment extends Fragment {

    private static NewProfileFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText hostname, port;
    private String selectedItem;
    private static ProfileORM profile;
    private Integer id;

    private Controller controller;

    public static NewProfileFragment getInstance(int sectionNumber, ProfileORM p, Controller controller) {

        if(INSTANCE==null) {

            NewProfileFragment fragment= new NewProfileFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }
        INSTANCE.controller= controller;
        profile= p;
        return INSTANCE;
    }

    public NewProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_profile, container, false);

        Axis.values();
        final Spinner typeSpinner = (Spinner) rootView.findViewById(R.id.type_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, new String[]{"hedgehog", "hedgehogdirect", "botball"});
        typeSpinner.setAdapter(spinnerArrayAdapter);

        // setting the listener to the spinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Warning: No axis selected", Toast.LENGTH_SHORT).show();
            }
        });

        hostname= (EditText) rootView.findViewById(R.id.new_profile_hostname_edit_text);
        port= (EditText) rootView.findViewById(R.id.new_profile_port_edit_text);

        final Button buttonsave= (Button) rootView.findViewById(R.id.save_profile);

        buttonsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(hostname.getText().toString().equals("") || port.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Textfiled is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO check the input, ip - address, port

                final Activity activity = getActivity();

                // check if edit or new
                if(profile==null) { // create a new profile
                    Log.d("Osiris", "New Profile");
                    ProfileORM porm= new ProfileORM(0, hostname.getText().toString(), Integer.valueOf(port.getText().toString()));
                    DBQuery.insertProfileItem(activity, porm);

                }else { // update the profile
                    ProfileORM porm= new ProfileORM(id, hostname.getText().toString(),Integer.valueOf(port.getText().toString()));
                    try {
                        DBQuery.updateProfileItem(activity, porm);
                    } catch (SQLException e) {
                        Log.d("Osiris", "Exception while updating", e);
                    }
                }

                // Jump to the ProfileFragement where all entries are listed.
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, ProfileFragment.getInstance(5, controller)).commit();

                // clear the textfildes
                hostname.setText("");
                port.setText("");
            }

        });

        // if profile != null, the user has pressed the update button
        if(profile != null) {
            //int spinnerPostion = spinnerArrayAdapter.getPosition(profile.getController_type());
            //typeSpinner.setSelection(spinnerPostion);

            hostname.setText(profile.getHostname(), TextView.BufferType.EDITABLE);
            port.setText(String.valueOf(profile.getPort()), TextView.BufferType.EDITABLE);
            id= profile.getId();
        }

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

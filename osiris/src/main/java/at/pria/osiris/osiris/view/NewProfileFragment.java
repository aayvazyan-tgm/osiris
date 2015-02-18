package at.pria.osiris.osiris.view;

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
import at.pria.osiris.osiris.controllers.ControllerType;
import at.pria.osiris.osiris.util.DataBaseHandler;
import at.pria.osiris.osiris.util.EnumUtil;
import at.pria.osiris.osiris.view.elements.Profile;

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
    private DataBaseHandler db;
    private ControllerType controllerType;
    private static Profile profile;

    public static NewProfileFragment getInstance(int sectionNumber, Profile p) {

        if(INSTANCE==null) {

            NewProfileFragment fragment= new NewProfileFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }

        profile= p;
        return INSTANCE;
    }

    public NewProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_new_profile, container, false);

        Axis.values();
        final Spinner typeSpinner = (Spinner) rootView.findViewById(R.id.type_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, EnumUtil.getEnumNames(ControllerType.class));
        typeSpinner.setAdapter(spinnerArrayAdapter);

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
                    Toast.makeText(getActivity(), "TextFiledsEmpty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO check the input, ip - address, port

                if(selectedItem.equals(ControllerType.Botball.toString())) {
                    controllerType= ControllerType.Botball;
                }
                if(selectedItem.equals(ControllerType.Hedgehog.toString())) {
                    controllerType= ControllerType.Hedgehog;
                }

                final Activity activity = getActivity();
                db= new DataBaseHandler(activity);

                // check if edit or new
                if(profile==null) { // create a new profile
                    Log.d("Osiris", "New Profile");
                    db.addProfile(new Profile(0, hostname.getText().toString(), Integer.valueOf(port.getText().toString()), controllerType));
                }else { // update the profile
                    db.update(profile.getId(), hostname.getText().toString(), Integer.valueOf(port.getText().toString()), controllerType);
                }

                // Jump to the ProfileFragement where all entries are listed.
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, ProfileFragment.getInstance(5)).commit();
            }

        });

        if(profile != null) {
            int spinnerPostion = spinnerArrayAdapter.getPosition(profile.getType().toString());
            typeSpinner.setSelection(spinnerPostion);

            hostname.setText(profile.getHost(), TextView.BufferType.EDITABLE);
            port.setText(String.valueOf(profile.getPort()), TextView.BufferType.EDITABLE);
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

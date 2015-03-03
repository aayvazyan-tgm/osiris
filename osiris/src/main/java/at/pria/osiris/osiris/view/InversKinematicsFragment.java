package at.pria.osiris.osiris.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;

/**
 * A Fragment to insert the invers kinematics values.
 * Created by helmuthbrunner on 03/03/15.
 */
public class InversKinematicsFragment extends Fragment {

    private static InversKinematicsFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG= "Osisir";

    private EditText xtext, ytext, ztext;
    private Button execute;

    public static InversKinematicsFragment getInstance(int sectionNumber) {

        if(INSTANCE==null) {
            InversKinematicsFragment fragment= new InversKinematicsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }

        return INSTANCE;
    }

    public InversKinematicsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inverskinecs, container, false);

        // get the EditText - Fields
        xtext= (EditText) rootView.findViewById(R.id.editTextX);
        ytext= (EditText) rootView.findViewById(R.id.editTextY);
        ztext= (EditText) rootView.findViewById(R.id.editTextZ);

        execute= (Button) rootView.findViewById(R.id.button_execute);

        // set a listener to the execute button
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String x= xtext.getText().toString();
                String y= ytext.getText().toString();
                String z= ztext.getText().toString();

                Log.d(TAG, "Values x:" + x + " y:" + y + " z:" + z);

                // check the format of the strings
                // send the values to the inverse kinematics function
            }
        });

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

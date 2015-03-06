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
import android.widget.Toast;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import org.apache.http.conn.ConnectTimeoutException;

/**
 * A Fragment to insert the invers kinematics values.
 * Created by helmuthbrunner on 03/03/15.
 */
public class InversKinematicsFragment extends Fragment {

    private static InversKinematicsFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG= "Osisir";

    private Controller robotController;

    private EditText xtext, ytext, ztext;
    private Button execute;

    public static InversKinematicsFragment getInstance(int sectionNumber, Controller robot) {

        if(INSTANCE==null) {
            InversKinematicsFragment fragment= new InversKinematicsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }
        INSTANCE.robotController=robot;

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

                // check the format of the strings
                String x= xtext.getText().toString();
                String y= ytext.getText().toString();
                String z= ztext.getText().toString();

                Log.d(TAG, "Values x:" + x + " y:" + y + " z:" + z);

                try {
                    robotController.getRobotArm().moveTo( Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
                } catch (ConnectionNotEstablishedException e) {
                    Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
                }
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

package at.pria.osiris.osiris.view.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import api.Axis;
import api.Position;
import at.pria.osiris.osiris.MainActivity;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.RobotArm;
import at.pria.osiris.osiris.view.elements.EmulatorView;

import java.util.ArrayList;

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

    private ArrayList<Position> savedPositions = new ArrayList<>();

    private final static String TAG = "Osiris";
    private LinearLayout teachLinLayout;


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
        rootView.findViewById(R.id.teachButtonTeach).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            executeTeachedPositions(savedPositions);
                            refreshView();
                        } catch (ConnectionNotEstablishedException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        rootView.findViewById(R.id.teachButtonTeach).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            saveNewPosition();
                            refreshView();
                        } catch (ConnectionNotEstablishedException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        teachLinLayout = (LinearLayout) rootView.findViewById(R.id.teaches);
        return rootView;
    }

    private void refreshView(){
        this.teachLinLayout.removeAllViews();

        LayoutInflater inflater;
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(Position pos : savedPositions){
            LinearLayout element = (LinearLayout) inflater.inflate(R.layout.teached_element ,
                    null);
            TextView text= (TextView) element.findViewById(R.id.textElement);
            for (int i = 0; i < pos.axesPositionInDegrees.length; i++) {
                text.setText(text.getText()+"-"+i+":"+pos.axesPositionInDegrees[i]);
            }
            this.teachLinLayout.addView(element);
        }
    }

    private void saveNewPosition() throws ConnectionNotEstablishedException {
        int availableAxes = Axis.values().length;
        double[] axesPos = new double[availableAxes];
        for (int i = 0; i < availableAxes; i++) {
            axesPos[i] = this.robotController.getRobotArm().getPosition(i);
        }
        this.savedPositions.add(new Position(axesPos));
    }

    private void executeTeachedPositions(ArrayList<Position> savedPositions) throws ConnectionNotEstablishedException {
        RobotArm emulator = EmulatorView.getInstance(getActivity());
        for (Position pos : savedPositions) {
            for (int i = 0; i < pos.axesPositionInDegrees.length; i++) {
                emulator.moveToAngle(i, (int) pos.axesPositionInDegrees[i]);
                try {
                    robotController.getRobotArm().moveToAngle(i, (int) pos.axesPositionInDegrees[i]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
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

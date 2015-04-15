package at.pria.osiris.osiris.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.botball.BotballController;
import at.pria.osiris.osiris.controllers.hedgehog.HedgehogController;
import at.pria.osiris.osiris.controllers.hedgehogdirect.HedgehogDirectController;
import at.pria.osiris.osiris.util.Storeage;

/**
 * A fragment to select which controller should be used.
 *
 * Created by helmuthbrunner on 15/04/15.
 */
public class SelectionFragment extends Fragment {

    private static SelectionFragment INSTANCE;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private final static String TAG= "Osiris";

    private Controller robotController;

    private ListView listView;
    private ArrayList<String> arrayList;

    /**
     * Creates a new DrawFragment instance
     * @param sectionNumber the sectionNumber from the fragments collection
     * @return the new instance
     */
    public static SelectionFragment getInstance(int sectionNumber, Controller robot) {

        if(INSTANCE==null) {

            SelectionFragment fragment= new SelectionFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE= fragment;
        }
        INSTANCE.robotController=robot;

        return INSTANCE;
    }

    public SelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selection, container, false);

        listView= (ListView) rootView.findViewById(R.id.listViewSelection);

        arrayList= new ArrayList<String>();

        arrayList.add("Hedgehog - Direct");
        arrayList.add("Hedgehog - Linker");
        arrayList.add("Botball");

        ListAdapter listAdapter= new ArrayAdapter<String>(getActivity(), R.layout.selection_element, arrayList);

        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    switch (position) {
                        case 0:
                            robotController= changeController(new HedgehogDirectController());
                            break;
                        case 1:
                            robotController= changeController(new HedgehogController());
                            break;
                        case 2:
                            robotController= changeController(new BotballController());
                            break;
                    }

                    Storeage.getInstance().setRobotController(robotController);
                }
            }
        );

        return rootView;
    }

    private Controller changeController(Controller controller) {
        return controller;
    }

}

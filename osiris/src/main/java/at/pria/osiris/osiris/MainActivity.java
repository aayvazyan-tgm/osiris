package at.pria.osiris.osiris;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.Toast;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.hedgehog.HedgehogController;
import at.pria.osiris.osiris.util.Storeage;
import at.pria.osiris.osiris.view.*;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Controller robotController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start by using a HedgehogController
        robotController = new HedgehogController();
        setUpController(robotController);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /* Setup the Andrix/Hedgehog controller */
        //Setup the network

    }

    public void setUpController(Controller controller) {
        try {
            controller.getSetup().setup(controller.getRobotArm());
            Storeage storeage = Storeage.getInstance();
            storeage.setRobotController(controller);
            this.robotController = controller;
        } catch (ConnectionNotEstablishedException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Connection not Established", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        Storeage storeage = Storeage.getInstance();
        robotController = storeage.getRobotController();

        if (position + 1 == 1) {        // controller
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ControllerFragment.getInstance(position + 1, robotController))
                    .commit();
        } else if (position + 1 == 2) { // inverse Kinematics
            fragmentManager.beginTransaction()
                    .replace(R.id.container, InversKinematicsFragment.getInstance(position + 1, robotController))
                    .commit();
        } else if (position + 1 == 3) { // sensor Values
            try {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TableSensorValuesFragment.getInstance(position + 1, robotController.getRobotArm()))
                        .commit();
            } catch (ConnectionNotEstablishedException e) {
                Toast.makeText(getBaseContext(), "Connection not yet Established", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (position + 1 == 4) { // draw line
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DrawFragment.getInstance(position + 1))
                    .commit();
        } else if (position + 1 == 5) { // profiles
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment.getInstance(position + 1, robotController))
                    .commit();
        } else if (position + 1 == 6) { // joystick
            fragmentManager.beginTransaction()
                    .replace(R.id.container, JoyStickFragment.getInstance(position + 1))
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.control);
                break;
            case 2:
                mTitle = getString(R.string.inversKinecs);
                break;
            case 3:
                mTitle = getString(R.string.sensor_values);
                break;
            case 4:
                mTitle = getString(R.string.drawline);
                break;
            case 5:
                mTitle = getString(R.string.profiles);
                break;
            case 6:
                mTitle = getString(R.string.joystick);
                break;
            case 7:
                mTitle = getString(R.string.settings);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFC107")));

        this.setStatusBarColor(getString(R.string.color_0));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(String color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_wifi) {
            // jump to the wifi selction screen
            this.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        }
        if (id == R.id.action_settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.
                    beginTransaction()
                    .replace(R.id.container, SettingsFragment.getInstance(7))
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

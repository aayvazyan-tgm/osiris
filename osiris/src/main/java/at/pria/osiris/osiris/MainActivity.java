package at.pria.osiris.osiris;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.hedgehogdirect.HedgehogDirectController;
import at.pria.osiris.osiris.util.Storeage;
import at.pria.osiris.osiris.view.NavigationDrawerFragment;
import at.pria.osiris.osiris.view.elements.EmulatorView;
import at.pria.osiris.osiris.view.fragments.*;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AndroidFragmentApplication.Callbacks {

    private final String TAG = "MAIN-Osiris";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Controller robotController;
    private boolean setupDone = false;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start by using a HedgehogController
//        setUpController(new HedgehogDirectController(),false);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        /* Setup the Andrix/Hedgehog controller */
        //Setup the network

        //Create the overlay
//        FragmentManager supportFragmentManager = getSupportFragmentManager();
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.overlay_fragment, RobotArmEmulatorFragment.getInstance(0))
//                .commit();
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.overlayLayout);
        final EmulatorView emulatorView = EmulatorView.getInstance(this);
        if (emulatorView.getParent() == null) frameLayout.addView(emulatorView);
        else {
            ViewGroup viewGroup = (ViewGroup) emulatorView.getParent();
            if (viewGroup.getId() == frameLayout.getId()) {
                viewGroup.removeView(emulatorView);
                frameLayout.addView(emulatorView);
            }
        }
    }

    public synchronized void setUpController(Controller controller, boolean newSetup) {
        if (setupDone == false || newSetup) {
            try {
                controller.getSetup().setup(controller.getRobotArm());
                Storeage storeage = Storeage.getInstance();
                storeage.setRobotController(controller);
                this.robotController = controller;
                this.setupDone = true;
                Toast.makeText(getBaseContext(), "Setup done: " + controller.getClass().getName(), Toast.LENGTH_LONG).show();
            } catch (ConnectionNotEstablishedException e) {
                setupDone = false;
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Connection not Established", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
//        setUpController(new HedgehogDirectController(), false);
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();

        Storeage storeage = Storeage.getInstance();
        robotController = storeage.getRobotController();
        if (robotController == null) {      //ask for a Controller if none is selected
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SelectionFragment.getInstance(position + 1, this))
                    .commit();
            mTitle = getString(R.string.selection);

        }else if (position + 1 == 1) {        // controller
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ControllerFragment.getInstance(position + 1, robotController))
                    .commit();
            mTitle = getString(R.string.control);
        } else if (position + 1 == 2) {         // inverse Kinematics
            fragmentManager.beginTransaction()
                    .replace(R.id.container, InversKinematicsFragment.getInstance(position + 1, robotController))
                    .commit();
            mTitle = getString(R.string.inversKinecs);
        } else if (position + 1 == 3) {         // sensor Values
            try {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TableSensorValuesFragment.getInstance(position + 1, robotController.getRobotArm()))
                        .commit();
            } catch (ConnectionNotEstablishedException e) {
                Toast.makeText(getBaseContext(), "Connection not yet Established", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            mTitle = getString(R.string.sensor_values);
        } else if (position + 1 == 4) { // draw line
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DrawFragment.getInstance(position + 1))
                    .commit();
            mTitle = getString(R.string.drawline);
        } else if (position + 1 == 5) { // profiles
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment.getInstance(position + 1, robotController))
                    .commit();
            mTitle = getString(R.string.profiles);
        } else if (position + 1 == 6) { // joystick
            fragmentManager.beginTransaction()
                    .replace(R.id.container, JoyStickFragment.getInstance(position + 1, robotController))
                    .commit();
            mTitle = getString(R.string.joystick);
        } else if (position + 1 == 7) { // QRReader
            fragmentManager.beginTransaction()
                    .replace(R.id.container, QRReaderFragment.getInstance(position + 1))
                    .commit();
            mTitle = getString(R.string.QRReader);
        } else if (position + 1 == 8) { // Emulator
            fragmentManager.beginTransaction()
                    .replace(R.id.container, EmulatorFragment.getInstance(position + 1))
                    .commit();
            mTitle = getString(R.string.Emulator);
        } else if (position + 1 == 9) { // Selection
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SelectionFragment.getInstance(position + 1, this))
                    .commit();
            mTitle = getString(R.string.selection);
        } else if (position + 1 == 10) { // Simulation
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SimulationFragment.getInstance(position + 1))
                    .commit();
            mTitle = getString(R.string.simulation);
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        }
        if (actionBar != null)
            actionBar.setTitle(mTitle);
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
            case 8:
                mTitle = getString(R.string.QRReader);
                break;
            case 9:
                mTitle = getString(R.string.Emulator);
                break;
            case 10:
                mTitle = getString(R.string.selection);
                break;
            case 11:
                mTitle = getString(R.string.simulation);
                break;
        }
    }

    public void restoreActionBar() {
        Resources res = getResources();
        int color = res.getColor(R.color.background_menu);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        int status_color = res.getColor(R.color.statusbar_color);

        this.setStatusBarColor(status_color);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
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

    @Override
    public void exit() {
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

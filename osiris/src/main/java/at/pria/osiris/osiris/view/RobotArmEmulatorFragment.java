package at.pria.osiris.osiris.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import at.pria.osiris.osiris.util.WIFIConnector;
import com.google.zxing.Result;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

/**
 * @author Ari Michael Ayvazyan
 * @version 29.10.2014
 */
public class RobotArmEmulatorFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static RobotArmEmulatorFragment INSTANCE;

    public RobotArmEmulatorFragment() {

    }

    public static RobotArmEmulatorFragment getInstance(int sectionNumber) {
        if (INSTANCE == null) {
            RobotArmEmulatorFragment fragment = new RobotArmEmulatorFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE = fragment;
        }

        return INSTANCE;

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
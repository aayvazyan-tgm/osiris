package at.pria.osiris.osiris.view.fragments;

import android.os.Bundle;
import android.widget.Toast;
import at.pria.osiris.osiris.util.WIFIConnector;
import com.google.zxing.Result;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

/**
 * @author Ari Michael Ayvazyan
 * @version 29.10.2014
 */
public class QRReaderFragment extends BarCodeScannerFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static QRReaderFragment INSTANCE;

    public QRReaderFragment() {

    }

    public static QRReaderFragment getInstance(int sectionNumber) {
        if (INSTANCE == null) {
            QRReaderFragment fragment = new QRReaderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE = fragment;
        }

        return INSTANCE;

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setmCallBack(new IResultCallback() {
            @Override
            public void result(Result lastResult) {
                Toast.makeText(getActivity(), "Connecting to: " + lastResult.toString(), Toast.LENGTH_SHORT).show();
                if(WIFIConnector.connectToOpenWifi(lastResult.getText(),getActivity()))
                    Toast.makeText(getActivity(), "Connected to: " + lastResult.toString(), Toast.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), "Could not connect to: " + lastResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //cameraManager.setManualFramingRect(450, 349, 100, 50);
    }
}
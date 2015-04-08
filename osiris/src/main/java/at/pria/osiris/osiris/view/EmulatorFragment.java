package at.pria.osiris.osiris.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import at.pria.osiris.osiris.R;
import at.pria.osiris.osiris.view.elements.EmulatorView;

/**
 * @author Ari Michael Ayvazyan
 * @version 29.10.2014
 */
public class EmulatorFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static EmulatorFragment INSTANCE;
    private EmulatorView emulatorView;
    private FrameLayout containerLayout;

    public EmulatorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.emulatorView = EmulatorView.getInstance(getActivity());
    }

    public static EmulatorFragment getInstance(int sectionNumber) {
        if (INSTANCE == null) {
            EmulatorFragment fragment = new EmulatorFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            INSTANCE = fragment;
        }

        return INSTANCE;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.containerLayout=new FrameLayout(getActivity());
        ViewGroup viewGroup = (ViewGroup) emulatorView.getParent();
        viewGroup.removeView(emulatorView);
        this.containerLayout.addView(emulatorView);
        return containerLayout;
    }

    @Override
    public void onDestroyView() {
        ViewGroup viewGroup = (ViewGroup) emulatorView.getParent();
        viewGroup.removeView(emulatorView);
        ViewGroup overlayLayout = (ViewGroup) getActivity().findViewById(R.id.overlayLayout);
        overlayLayout.addView(emulatorView);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setBackgroundColor(79);
    }
}
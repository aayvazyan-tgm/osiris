package at.pria.osiris.osiris.view.elements;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Button;

/**
 * Visualises activity
 *
 * @author Ari Michael Ayvazyan
 * @version 09.11.2014
 */
public class ButtonVisualiser implements VisualisingActivity {
    private Button button;
    private Drawable backgroundBefore;
    private boolean isVisualising = false;
    private Drawable backgroundDisplayingActivity;

    /**
     * @param button the button that should be visualising activity
     */
    public ButtonVisualiser(Button button) {
        this(button, new ColorDrawable(Color.parseColor("#ff5722")));
    }

    /**
     * @param button                       the button that should be visualising activity
     * @param backgroundDisplayingActivity defines how the buttons background should look like when displaying activity
     */
    public ButtonVisualiser(Button button, Drawable backgroundDisplayingActivity) {
        if (button == null) throw new IllegalArgumentException("button may not be null");
        if (backgroundDisplayingActivity == null)
            throw new IllegalArgumentException("backgroundDisplayingActivity may not be null");
        this.button = button;
        this.backgroundDisplayingActivity = backgroundDisplayingActivity;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public synchronized void startVisualise() {
        if (!isVisualising()) {
            isVisualising = true;
            this.backgroundBefore = this.button.getBackground();

            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //noinspection deprecation
                this.button.setBackgroundDrawable(this.backgroundDisplayingActivity);
            } else {
                this.button.setBackground(this.backgroundDisplayingActivity);
            }
        }
    }

    /**
     * @return returns true if the button is currently visualising activity.
     */
    public boolean isVisualising() {
        return isVisualising;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public synchronized void stopVisualise() {
        if (isVisualising) {
            isVisualising = false;

            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //noinspection deprecation
                this.button.setBackgroundDrawable(this.backgroundBefore);
            } else {
                this.button.setBackground(this.backgroundBefore);
            }
        }
    }
}
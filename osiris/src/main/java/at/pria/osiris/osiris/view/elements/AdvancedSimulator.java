package at.pria.osiris.osiris.view.elements;

import android.content.Context;
import android.view.View;
import at.pria.osiris.OsirisSimulation;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * @author Ari Michael Ayvazyan
 * @version 23.04.2015
 */
public class AdvancedSimulator extends AndroidApplication{
    public View getGameView(){
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        OsirisSimulation osirisSimulation = new OsirisSimulation();
        return initializeForView(osirisSimulation, config);
    }
}

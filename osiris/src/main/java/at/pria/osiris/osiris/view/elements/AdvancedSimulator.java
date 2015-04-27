package at.pria.osiris.osiris.view.elements;

import android.content.Context;
import android.view.View;
import at.pria.osiris.OsirisSimulation;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * @author Ari Michael Ayvazyan, Samuel Schmidt
 * @version 27.04.2015
 */
public class AdvancedSimulator extends AndroidApplication{
    public View getGameView(){
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        OsirisSimulation osirisSimulation = new OsirisSimulation();
        // has to be non-static for initializeForView so not sure how to Singleton
        return initializeForView(osirisSimulation, config);
    }
}

package at.pria.osiris.osiris;

import android.view.MotionEvent;
import android.view.View;
import at.pria.osiris.osiris.api.Axis;
import at.pria.osiris.osiris.api.Robotarm;

/**
 * @author Ari Ayvazyan
 * @version 27.10.2014
 */
public class DemoOntouchListener implements View.OnTouchListener{
    private int power = 100;

    private Robotarm robotarm;

    public DemoOntouchListener(Robotarm robotarm){
        this.robotarm = robotarm;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            robotarm.turnAxis(Axis.AXISTWO, power);
            Thread.sleep(1000);
            robotarm.stopAxis(Axis.AXISTWO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}

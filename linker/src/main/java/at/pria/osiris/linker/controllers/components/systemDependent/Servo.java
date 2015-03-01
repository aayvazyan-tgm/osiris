package at.pria.osiris.linker.controllers.components.systemDependent;

/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public interface Servo {

    public void moveToExactPosition(int position);

    public int getPosition();

    public int getMaximumAngle();
}

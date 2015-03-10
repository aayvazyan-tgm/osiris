package at.pria.osiris.linker.controllers.components.systemDependent;

/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public interface Servo {

    /**
     *
     * @param position
     */
    public void moveToAngle(int position);

    /**
     *
     * @return return the position in degrees
     */
    public int getPositionInDegrees();

    /**
     *
     * @return return the maximum position in degrees
     */
    public int getMaximumAngle();

    /**
     *
     * @return return the required time to turn the servo by one degree
     */
    public long getTimePerDegreeInMilli();
}

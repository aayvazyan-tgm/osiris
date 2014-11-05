package at.pria.osiris.osiris.api;

/**
 * Specifies the minimum and maximum angle for an axis and is used to identify an axis
 *
 * @author Adrian Bergler
 * @version 2014-10-17
 */
public enum Axis {
    BASE(-1, -1),
    AXISONE(0, 100),
    AXISTWO(0, 100);

    private final int minimumAngle;
    private final int maximumAngle;

    Axis(int minimumAngle, int maximumAngle) {
        this.minimumAngle = minimumAngle;
        this.maximumAngle = maximumAngle;
    }

    public int getMinimumAngle() {
        return minimumAngle;
    }

    public int getMaximumAngle() {
        return maximumAngle;
    }

}

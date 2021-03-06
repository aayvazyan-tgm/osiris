package api;

/**
 * Specifies the minimum and maximum angle for an axis and is used to identify an axis
 *
 * @author Adrian Bergler
 * @version 2014-10-17
 */
@Deprecated
public enum Axis {
    BASE(-12345678, 12345678),
    AXISONE(150, 800),
    AXISTWO(10, 1000),
    AXISTHREE(10, 1000);

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

package at.pria.osiris.osiris.view.elements.joystick;

/**
 * https://code.google.com/p/mobile-anarchy-widgets/source/checkout
 * source: svn checkout http://mobile-anarchy-widgets.googlecode.com/svn/trunk/ mobile-anarchy-widgets-read-only
 */
public interface JoystickMovedListener {
    public void OnMoved(int pan, int tilt);
    public void OnReleased();
    public void OnReturnedToCenter();
}

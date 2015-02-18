package at.pria.osiris.linker.implementation.hedgehog.axes;

import at.pria.osiris.linker.controllers.components.Axis;
import at.pria.osiris.linker.controllers.components.systemDependent.Servo;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogServo;
import org.andrix.low.NotConnectedException;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class BaseAxis extends Axis{
    private Servo servo;
    private int servoPosition=-1; //-1 is a undefined state
    public BaseAxis() throws NotConnectedException {
        super("BaseAxis");
        this.servo=new HedgehogServo(1);
    }

    /**
     *
     * @see at.pria.osiris.linker.controllers.components.Axis
     */
    public void moveToPosition(int position){
        try {
            servo.moveToAngle(position);
            servoPosition=position;
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see at.pria.osiris.linker.controllers.components.Axis
     */
    public void moveAtPower(int power){
        //TODO This one will be tricky...
    }

    /**
     * @see at.pria.osiris.linker.controllers.components.Axis
     */
    @Override
    public int getSensorValue() {
        return servoPosition;
    }
}

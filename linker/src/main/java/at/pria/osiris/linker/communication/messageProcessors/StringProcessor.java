package at.pria.osiris.linker.communication.messageProcessors;

import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axes.Axis;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class StringProcessor implements MessageProcessor {
    private RobotArm robotarm;

    public StringProcessor(RobotArm robotarm) {
        this.robotarm = robotarm;
        System.out.println("String processor initialized");
    }

    private void processMessage(String message) {
        System.out.println("Received: " + message);
        String[] splitted = message.split("/");
        for (int i = 0; i < splitted.length; i++) {
//            System.out.println("Splitted" + i + ": " + splitted[i]);
        }
        if (splitted[0].equals("turnaxis") && splitted.length == 3) {
            try {
//                System.out.println("Turning axis...");
                robotarm.getAxis(Integer.parseInt(splitted[1]))
                        .moveAtPower(Integer.parseInt(splitted[2]));
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        } else if (splitted[0].equals("turnaxis") && splitted.length == 4) {
            try {
                System.out.println("Turning axis...");
                robotarm.getAxis(Integer.parseInt(splitted[1]))
                        .moveAtPower(Integer.parseInt(splitted[2]));

            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        } else if (splitted[0].equals("stopaxis") && splitted.length == 2) {
            try {
//                System.out.println("stopaxis");
                robotarm.getAxis(Integer.parseInt(splitted[1])).moveAtPower(0);

            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        } else if (splitted[0].equals("moveto") && splitted.length == 4) {
            try {
//                System.out.println("moveto");
                robotarm.moveTo(Double.parseDouble(splitted[1]),
                        Double.parseDouble(splitted[2]),
                        Double.parseDouble(splitted[3]));

            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        } else if (splitted[0].equals("stopall") && splitted.length == 1) {
//            System.out.println("stopall");
            for (Axis axis : robotarm.getAvailableAxes()) {
                axis.moveAtPower(0);
            }
        } else if (splitted[0].equals("close") && splitted.length == 1) {
//            System.out.println("close");

        } else if (splitted[0].equals("test") && splitted.length == 1) {
//            System.out.println("test");
        }
    }

    @Override
    public void processMessage(Object message) {
        if (message instanceof String) processMessage((String) message);
    }
}

package oldLinker.control.MessageProcessor;

import api.Axis;
import api.Robotarm;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class StringProcessor implements MessageProcessor {
    private Robotarm robotarm;

    public StringProcessor(Robotarm robotarm) {
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
                robotarm.turnAxis(Axis.values()[Integer.parseInt(splitted[1])],
                        Integer.parseInt(splitted[2]));
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        } else if (splitted[0].equals("turnaxis") && splitted.length == 4) {
            try {
                System.out.println("Turning axis...");
                robotarm.turnAxis(Axis.values()[Integer.parseInt(splitted[1])],
                        Integer.parseInt(splitted[2]),
                        Integer.parseInt(splitted[3]));

            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        } else if (splitted[0].equals("stopaxis") && splitted.length == 2) {
            try {
//                System.out.println("stopaxis");
                robotarm.stopAxis(Axis.values()[Integer.parseInt(splitted[1])]);

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
            robotarm.stopAll();

        } else if (splitted[0].equals("close") && splitted.length == 1) {
//            System.out.println("close");
            robotarm.close();

        } else if (splitted[0].equals("test") && splitted.length == 1) {
//            System.out.println("test");
            robotarm.test();
        }
    }

    @Override
    public void processMessage(Object message) {
        if (message instanceof String) processMessage((String) message);
    }
}

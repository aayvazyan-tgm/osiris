package at.pria.osiris.osiris.util;

/**
 * @author Ari Michael Ayvazyan
 * @version 04.11.2014
 */
public class EnumUtil {
    public static String[] getEnumNames(Class<? extends Enum<?>> e) {
        Enum[] states = e.getEnumConstants();
        String[] names = new String[states.length];

        for (int i = 0; i < states.length; i++) {
            names[i] = states[i].name();
        }
        return names;
    }
}

package at.pria.osiris.linker.controllers.hedgehog;

import java.util.HashMap;

/**
 * The AxisDefinition class, is used to store the defined axis
 *
 * Created by helmuthbrunner on 07/01/15.
 */
public class AxisDefinition implements at.pria.osiris.linker.controllers.components.systemDependent.AxisDefinition {

    private static AxisDefinition instance;
    private HashMap<Integer, String> map;

    public static AxisDefinition getInstance() {
        if(instance==null)
            instance= new AxisDefinition();
        return instance;
    }

    private AxisDefinition() {
        map= new HashMap<Integer, String>();
    }

    @Override
    public boolean addAxis(int id, String name) {
        if(map.put(id, name) == null)
            map.put(id, name);
        else
            return false;
        return true;//TODO test if this works
    }

    @Override
    public boolean removeAxis(int id) {
        map.remove(id);
        return true;//TODO return statement necessary???
    }

    /**
     * If the value is null, the map contains no mapping for the key
     * @param id the key
     * @return the name
     */
    @Override
    public String getName(int id) {
        return map.get(id);
    }

}

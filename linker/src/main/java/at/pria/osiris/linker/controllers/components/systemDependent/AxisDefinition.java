package at.pria.osiris.linker.controllers.components.systemDependent;

/**
 * The AxisDefinition interface
 *
 * Created by helmuthbrunner on 07/01/15.
 */
public interface AxisDefinition {

    public boolean addAxis(int id, String name);

    public boolean removeAxis(int id);

    public String getName(int id);

}
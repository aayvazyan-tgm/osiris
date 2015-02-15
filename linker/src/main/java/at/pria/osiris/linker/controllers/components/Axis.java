package at.pria.osiris.linker.controllers.components;


import at.pria.osiris.linker.controllers.components.systemDependent.AxisDefinition;

/**
 *
 * @author Helmuth Brunner
 * @version 2015-01-07
 */
public class Axis {

	private String name;
	private int axisID;
	private AxisDefinition hhad;

	public Axis(int axisID, AxisDefinition hhad) {
		this.hhad= hhad;
		this.name= this.hhad.getName(axisID);
		this.axisID= axisID;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return axisID;
	}

	public String getName(int axisID) {
		return hhad.getName(axisID);
	}

}

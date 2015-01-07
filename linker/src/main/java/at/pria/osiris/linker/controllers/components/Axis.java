package at.pria.osiris.linker.controllers.components;

import at.pria.osiris.linker.controllers.hedgehog.HedgehogAxisDef;

/**
 *
 * @author Helmuth Brunner
 * @version 2015-01-07
 */
public class Axis {

	private String name;
	private int axisID;
	private HedgehogAxisDef hhad;

	public Axis(int axisID, HedgehogAxisDef hhad) {
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

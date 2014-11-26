package org.andrix.listeners;

import java.util.ArrayList;
import java.util.List;

import org.andrix.low.ConnectionState;
import org.andrix.low.HardwareController;

public interface StateListener {
	public static List<StateListener> _l_state = new ArrayList<StateListener>();

	public void connectionStateChange(ConnectionState state, HardwareController hwc);

	public void scanUpdate(HardwareController hwc);

	public void exceptionThrown(Exception ex);
}

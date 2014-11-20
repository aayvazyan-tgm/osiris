package org.andrix.listeners;

import java.util.ArrayList;
import java.util.List;

public interface AXCPListener {
	
	public static List<AXCPListener> _l_AXCP = new ArrayList<AXCPListener>();
	
	public void received(byte opcode, byte[] payload);

	public void sent(byte opcode, byte[] payload);
}

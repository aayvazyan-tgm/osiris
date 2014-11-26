package org.andrix;

import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

public interface AXCPHandler {
	public boolean handleCommand(byte opcode, Object... params) throws NotConnectedException, RequestTimeoutException;
}

/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.low;

/**
 * Interface ServerListener<br>
 * Provides methods which get called in case of server actions.
 * 
 * @author Christoph Krofitsch
 * @version 17.09.2012
 */
public interface ServerListener {

	public void onClientDisconnect(AXCPServer server);

	/**
	 * Called when data is received from the client.
	 * 
	 * @param client
	 *            source client
	 * @param data
	 *            data
	 */
	public void onReceive(AXCPServer server, byte opcode, byte[] payload);
}

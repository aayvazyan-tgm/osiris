/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.low;

public enum ConnectionState {
	CONNECTED_NOAUTH, CONNECTED_AUTH, DISCONNECTED;

	public String toString() {
		switch (this) {
		case CONNECTED_NOAUTH:
			return "connected and not authenticated";
		case CONNECTED_AUTH:
			return "connected and authenticated";
		case DISCONNECTED:
			return "disconnected";
		}
		throw new AssertionError("Unknown ConnectionState!");
	}
}

/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.low;

import java.io.IOException;

public class RequestTimeoutException extends IOException {

	private static final long serialVersionUID = 812341743242364582L;

	public RequestTimeoutException() {
		super();
	}

	public RequestTimeoutException(String detailMessage) {
		super(detailMessage);
	}
}

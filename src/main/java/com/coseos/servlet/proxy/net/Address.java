/** Address
 * 
 * URL like class with protocol, host and port properties to address proxy and
 * web server that the proxy forwards requests to
 *  
 * Copyright (c) 2019 by Thorsten J. Lorenz / coseos
 *  
 * All rights reserved. Alle Rechte vorbehalten.
 *  
 * Read and respect the license as documented in the /LICENSE.txt file supplied with
 * this code. Distribution of this file without the LICENSE.txt file is in conflict
 * with the license of this software.
 *  
 * Please send reports about a license violation to license-violation@coseos.com 
 */
package com.coseos.servlet.proxy.net;

import static java.lang.String.format;

public class Address {
	
	private static final int DEFAULT_PORT = 80;
	public enum Protocol { HTTP, HTTPS }

	private Address(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public static Address address(final String host, final int port) {
		return new Address(host, port);
	}

	public static Address address(final String host) {
		return new Address(host, DEFAULT_PORT);
	}
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public String getAsHostHeader() {
		return format("%s%s%s",				
			host,
			(port == DEFAULT_PORT)?"":":",
			(port == DEFAULT_PORT)?"":Integer.toString(port));
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Address)) return false;
		Address address = (Address)o;
		if(this==address) return true;
		if(host.equals(address.getHost()) && port == address.getPort()) return true;
		// TODO: refine definition of equal for class Address
		return false;
	}
	
	@Override
	public String toString() {
		return format("http%s://%s",
			(protocol == Protocol.HTTP)?"":"s", // for https only
			getAsHostHeader());
	}

	private final Protocol protocol = Protocol.HTTP; // default to HTTP
	private final String host;
	private final int port;
}

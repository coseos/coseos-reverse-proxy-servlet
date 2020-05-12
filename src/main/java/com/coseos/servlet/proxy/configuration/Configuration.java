/** Configuration
 * 
 * The coseos reverse servlet configuration class
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
package com.coseos.servlet.proxy.configuration;

import static com.coseos.servlet.proxy.net.Address.address;

import java.util.logging.Logger;

import com.coseos.servlet.proxy.net.Address;

public class Configuration {

	private Configuration(final Address proxy, final Address web) {
		
		logger.info(String.format("Proxy is %s", proxy.toString()));
		logger.info(String.format("Web is %s",web.toString()));
		
		this.proxy = proxy;
		this.web = web;
	}
	
	public Address getProxy() {
		return proxy;
	}
	public Address getWeb() {
		return web;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Builder() {
			
		}
		
		public Builder setProxy(final String host, final int port) {
			this.proxy = address(host, port);
			return this;
		}

		public Builder setWeb(final String host, final int port) {
			this.web = address(host, port);
			return this;
		}
		
		public Configuration build() {
            return new Configuration(proxy, web);			
		}

		private Address proxy;
		private Address web;
	}
	
	private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
	
	private Address proxy;
	private Address web;
}

/** Configuration Test
 * 
 * The Configuration class test
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.coseos.servlet.proxy.configuration.Configuration;

public class ConfigurationTest {

	private static final String PROXY_HOST = "proxy";
	private static final int PROXY_PORT = 8080;
	private static final String WEB_HOST = "web";
	private static final int WEB_PORT = 8888;
	
	@Test
	public void simpleTest() {
		Configuration configuration = Configuration.builder()
			.setProxy(PROXY_HOST, PROXY_PORT)
			.setWeb(WEB_HOST, WEB_PORT).build();
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.getProxy().getHost(), is(equalTo(PROXY_HOST)));
		assertThat(configuration.getProxy().getPort(), is(equalTo(PROXY_PORT)));
		assertThat(configuration.getWeb().getHost(), is(equalTo(WEB_HOST)));
		assertThat(configuration.getWeb().getPort(), is(equalTo(WEB_PORT)));
		
		assertThat(configuration.getProxy().toString(),is(equalTo("nix")));
		
	}
}

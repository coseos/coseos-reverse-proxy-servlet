/** AddressTest
 * 
 * The Address class test
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

import static org.hamcrest.MatcherAssert.assertThat;
import static com.coseos.servlet.proxy.net.Address.address;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.coseos.servlet.proxy.net.Address;

public class AddressTest {

	private static final String DEFAULT_HOST = "default.host";
	private static final int DEFAULT_PORT = 8888;
	
	@Test
	public void simpleTest() {		
		Address address = address(DEFAULT_HOST, DEFAULT_PORT);

		assertThat(address,is(notNullValue()));
		assertThat(address.getHost(),is(equalTo(DEFAULT_HOST)));
		assertThat(address.getPort(),is(equalTo(DEFAULT_PORT)));
		assertThat(address.toString(),is(equalTo(String.format("http://%s:%s", DEFAULT_HOST, DEFAULT_PORT))));
	}
}

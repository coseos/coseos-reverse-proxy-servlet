/** ReverseProxyServlet
 * 
 * The coseos reverse servlet proxy URLMapper class
 *  
 * Copyright (c) 2019 by Thorsten J. Lorenz / coseos
 *  
 * All rights reserved. Alle Rechte vorbehalten.
 *  
 * Read and respect the license as documented in the /LICENSE.txt file supplied with
 * this code. Distribution of this file without the LICENSE.txt file is in conflict
 * with the license of this software.
 *  
 *  
 * Please send reports about a license violation to license-violation@coseos.com 
 */
package com.coseos.servlet.proxy.mapper;

import java.net.MalformedURLException;
import java.net.URL;

import com.coseos.servlet.proxy.configuration.Configuration;

public class URLMapper {

	public URLMapper(final Configuration configuration) {
		this.configuration = configuration;
	}
	
	public URL map(URL requestUrl) throws MalformedURLException {
		URL result = new URL(
			String.format("%s%s",
				configuration.getWeb().toString(),
				requestUrl.getPath()));
		return result;
	}
	
	private Configuration configuration;	
}

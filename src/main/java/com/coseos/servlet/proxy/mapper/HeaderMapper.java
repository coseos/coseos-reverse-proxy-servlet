/** ReverseProxyServlet
 * 
 * The coseos reverse servlet proxy HeaderMapper class
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
package com.coseos.servlet.proxy.mapper;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.coseos.servlet.proxy.configuration.Configuration;

import okhttp3.Headers;
import okhttp3.Headers.Builder;

public class HeaderMapper {

	private static final String HOST = "Host:";

	public HeaderMapper(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public Headers map(HttpServletRequest httpServletRequest) {
		Builder builder = new Headers.Builder();
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = httpServletRequest.getHeader(headerName);
			
			if(headerName.equals(HOST)) {
				headerValue = configuration.getWeb().getAsHostHeader();
			}			
			builder.add(headerName, headerValue);
		}		
		return builder.build();		
	}
	
	private Configuration configuration;
}

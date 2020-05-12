/** ReverseProxyServlet
 * 
 * The coseos reverse servlet proxy RequestMapper class
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

import static java.lang.String.format;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.coseos.servlet.proxy.configuration.Configuration;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class RequestMapper {

	public RequestMapper(Configuration configuration) {
		this.configuration = configuration;
		urlMapper = new URLMapper(configuration);
		headerMapper = new HeaderMapper(configuration);
	}

	public Request map(HttpServletRequest httpServletRequest) throws MalformedURLException {
		URL requestURL = new URL(httpServletRequest.getRequestURL().toString());
		HttpUrl.Builder urlBuilder = HttpUrl.parse(urlMapper.map(requestURL).toString()).newBuilder();

		logger.info(format("from %s to %s", requestURL.toString(), urlBuilder.build().toString()));

		if(hasBody(httpServletRequest.getMethod())) {
			try {
				return new Request.Builder()
					.url(urlBuilder.build().toString())
					.headers(headerMapper.map(httpServletRequest))
					.method(httpServletRequest.getMethod(), bodyMapper.map(httpServletRequest))
					.build();
			} catch(IOException ioException) {
				logger.warning(ioException.getMessage());
			}
			// TODO: this fall-through is not the best solution for a failing POST or PUT
		} 
		return new Request.Builder()
			.url(urlBuilder.build().toString())
		    .headers(headerMapper.map(httpServletRequest))
		    .build();
	}
	
	/** hasBody
	 * 
	 * TODO: Fix this code - depending on the method for body detection is not the best method
	 * 
	 * @param method a HTTP method
	 * @return true for a method that has a body
	 */
	private boolean hasBody(final String method) {
		return("POST".equals(method)||"PUT".equals(method));
	}

	private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
	
	Configuration configuration;
	URLMapper urlMapper;
	HeaderMapper headerMapper;
	BodyMapper bodyMapper = new BodyMapper();
}

/** ReverseProxyServlet
 * 
 * The coseos reverse servlet proxy class
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
package com.coseos.servlet.proxy;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coseos.servlet.proxy.configuration.Configuration;
import com.coseos.servlet.proxy.mapper.RequestMapper;
import com.coseos.servlet.proxy.mapper.ResponseMapper;

import okhttp3.OkHttpClient;

public class ReverseProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * init
	 * 
	 * init create the request mapper and the response mapper objects.
	 * 
	 * configuration is read from WEB-INF/web.xml init params
	 * - proxy-host / proxy-port for the proxy and
	 * - web-host / web-port for the server behind the proxy
	 * 
	 * default values for proxy host are localhost:8080 (assumes the
	 * proxy runs as a servlet in a local tomcat with default port)
	 * 
	 * default values for web host are localhost:4567 (assumes the web
	 * server runs as spark java microserver application on localhost)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);		
		
		final String proxyHost = config.getInitParameter("proxy-host"); 
		final String proxyPort = config.getInitParameter("proxy-port"); 
		final String webHost = config.getInitParameter("web-host"); 
		final String webPort = config.getInitParameter("web-port"); 
		
		setOkHttpClient(new OkHttpClient());
		setConfiguration(Configuration.builder()
			.setProxy(proxyHost!=null?proxyHost:"localhost",
					  proxyPort!=null?Integer.parseUnsignedInt(proxyPort):8080)
			.setWeb(webHost!=null?webHost:"localhost",
					webPort!=null?Integer.parseUnsignedInt(webPort):4567)
			.build());
		setRequestMapper(new RequestMapper(configuration));
		setResponseMapper(new ResponseMapper());
	}

	ReverseProxyServlet setOkHttpClient(final OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
		return this;
	}	
	
	ReverseProxyServlet setConfiguration(final Configuration configuration) {
		this.configuration = configuration;
		return this;
	}	
	
	ReverseProxyServlet setRequestMapper(final RequestMapper requestMapper) {
		this.requestMapper = requestMapper;
		return this;
	}
	
	ReverseProxyServlet setResponseMapper(final ResponseMapper responseMapper) {
		this.responseMapper = responseMapper;
		return this;
	}

	/**
	 * destroy
	 * 
	 * destroy tries to evict all from the OkHTTP client connection pool.
	 * 
	 * this does not avoid the "appears to have started a thread named [OkHttp ConnectionPool]
	 * but has failed to stop it." WARNING from tomcat, but we do our best 
	 */
	@Override
	public void destroy() {
		okHttpClient.connectionPool().evictAll();
		okHttpClient = null;
	}	

	/**
	 * doGet
	 * 
	 * servlet handler that maps from javax.servlet to okhttp request/response
	 * and acts as a proxy.
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException {
		try {
			responseMapper.map(
				httpServletResponse, okHttpClient.newCall(requestMapper.map(httpServletRequest))
				    .execute());
			return;
		} catch (IOException e) {
			this.logger.warning(e.getMessage());
		}
		httpServletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
	}
	
	private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

	private OkHttpClient okHttpClient;
	private Configuration configuration;
	private RequestMapper requestMapper;
	private ResponseMapper responseMapper;
}

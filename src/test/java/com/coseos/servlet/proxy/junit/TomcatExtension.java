/** TomcatExtension
 * 
 * A JUnit5 extension to run tomcat and register a servlet
 *  
 * Copyright (c) 2019 by Thorsten J. Lorenz / coseos
 *  
 * This software is MIT licensed. All rights reserved. Alle Rechte vorbehalten.
 * 
 * The MIT License (SPDX short identifier: MIT)
 *
 * Copyright 2019 (c) Thorsten J. Lorenz / coseos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 * 
 * Please respect the license. Distribution of this file without the LICENSE.txt file is in conflict
 * with the license of this software.
 *  
 * Please send reports about a license violation to license-violation@coseos.com 
 */
package com.coseos.servlet.proxy.junit;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**  TomcatExtension
 * 
 * Usage:
 *
 * In your unit test class, add the extension as a static field with:
 * 
 * 	  @RegisterExtension
 *    static TomcatExtension tomcatExtension = TomcatExtension.builder()
 *        .host("localhost")
 *        .port(8070)
 *        .clazz(ReverseProxyServlet.class)
 *        .build();
 * 
 * Note:
 * 
 * "tomcat.getServer().await();" will block and wait for tomcat shutdown.
 *
 * @author tlorenz
 */
public class TomcatExtension  implements BeforeAllCallback, AfterAllCallback {

	private Tomcat tomcat;
	private final Class<?> clazz;
	private final String host;
	private final int port;
	private final String appBase;
	private final String docBase;
	private final String context;
	private final String mapping;
	private final int wait;
	
	private TomcatExtension(final Class<?> clazz, final String host, final int port, final String appBase, final String docBase, final String context, final String mapping, final int wait) {
		this.clazz = clazz;
		this.host = host;
		this.port = port;
		this.appBase = appBase;
		this.docBase = docBase;
		this.context = context;
		this.mapping = mapping;
		this.wait = wait;
	}

	@Override
	public void afterAll(ExtensionContext arg0) throws Exception {
		tomcat.stop();
	}

	@Override
	public void beforeAll(ExtensionContext arg0) throws Exception {
		tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.setHostname(host);
		tomcat.getHost().setAppBase(appBase);
		
		File docBase = new File(this.docBase);
		Context context = tomcat.addContext(this.context, docBase.getAbsolutePath());
		Tomcat.addServlet(context, clazz.getSimpleName(),clazz.getName());
		context.addServletMappingDecoded(mapping, clazz.getSimpleName());

		tomcat.start();
		Thread.sleep(wait);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {

		private String host = "localhost";
		private Class<?> clazz = DefaultServlet.class;
		private int port = 8086;
		private int wait = 1000;
		private String appBase = ".";
		private String docBase = ".";
		private String context = "";
		private String mapping = "/";

		public Builder clazz(final Class<?> clazz) {
			this.clazz = clazz;
			return this;
		}
		
		public Builder appBase(final String appBase) {
			this.appBase = appBase;
			return this;
		}

		public Builder docBase(final String docBase) {
			this.docBase = docBase;
			return this;
		}

		public Builder context(final String context) {
			this.context = context;
			return this;
		}

		public Builder mapping(final String mapping) {
			this.mapping = mapping;
			return this;
		}
		
		public Builder host(final String host) {
			this.host= host;
			return this;
		}
		
		public Builder port(final int port) {
			this.port= port;
			return this;
		}
		
	    public Builder wait(final int wait) {
		    this.wait = wait;
		    return this;
	    }
		
		public TomcatExtension build() {
			return new TomcatExtension(clazz,host,port,appBase,docBase,context,mapping,wait);
		}
	}
}

/** ReverseProxyServletTest
 * 
 * The ReverseProxyServlet class test
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.coseos.servlet.proxy.junit.TomcatExtension;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// @ExtendWith(TomcatExtension.class)
public class ReverseProxyServletTest {

	@RegisterExtension
    static TomcatExtension tomcatExtension = TomcatExtension.builder()
        .host("localhost")
        .port(8070)
        .clazz(ReverseProxyServlet.class)
        .build();
	
	public static OkHttpClient client = new OkHttpClient();
	
	@Test
	public void servletTest() throws InterruptedException, IOException {
		System.out.println("Test tomcat on 8070");
		
		Request request = new Request.Builder().url("http://localhost:8070/docs/").build();

		Call call = client.newCall(request);
		Response response = call.execute();	
		
		assertThat(response,is(notNullValue()));
		assertThat(response.code(),is(200));
		assertThat(response.header("Content-Type"),is(equalTo("text/html")));
		assertThat(response.body().string(),startsWith("<!DOCTYPE html SYSTEM "));
		
		Thread.sleep(1000);
	}
	
}

/** ReverseProxyServlet
 * 
 * The coseos reverse servlet proxy BodyMapper class
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BodyMapper {

	/**
	 * 
	 *     RequestBody.create (deprecated), but UByteArrayKt not useable (has to be resolved)
	 * 
	 * @param httpServletRequest
	 * @return
	 * @throws IOException
	 */
    @SuppressWarnings("deprecation")
	public RequestBody map(final HttpServletRequest httpServletRequest) throws IOException {
        ServletInputStream servletInputStream = httpServletRequest.getInputStream();
        byte[] buffer = new byte[servletInputStream.available()];
        int byteCount = servletInputStream.read();

        MediaType contentType = MediaType.parse(httpServletRequest.getContentType());

        // UByteArrayKt.toRequestBody(contentType, buffer, 0, byteCount);
        return RequestBody.create(contentType, buffer, 0, byteCount);
	}
	
	/**
	 * @see https://stackoverflow.com/questions/43157/easy-way-to-write-contents-of-a-java-inputstream-to-an-outputstream
	 * 
	 *      The JDK uses the same code so it seems like there is no "easier" way
	 *      without clunky third party libraries The following is directly copied
	 *      from java.nio.file.Files.java
	 * 
	 *      Reads all bytes from an input stream and writes them to an output
	 *      stream.
	 */
	static long copy(InputStream source, OutputStream sink) throws IOException {
		long nread = 0L;
		byte[] buf = new byte[BUFFER_SIZE_MEDIOCRE];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			nread += n;
		}
		return nread;
	}

	@SuppressWarnings("unused") private static final int BUFFER_SIZE_SMALL = 1024;	
	private static final int BUFFER_SIZE_MEDIOCRE = 8192;	
	@SuppressWarnings("unused")  private static final int BUFFER_SIZE_LARGE = 65536;	
}

/** ReverseProxyServlet
 * 
 * The coseos reverse servlet proxy ResponseMapper class
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

import static java.lang.String.join;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import okhttp3.Response;

public class ResponseMapper {
	public ResponseMapper map(HttpServletResponse httpServletResponse, Response response) throws IOException {
		if (response.isSuccessful()) {
			httpServletResponse.setStatus(response.code());
			response.headers().names()
					.forEach(name -> httpServletResponse.addHeader(name, join(" ", response.headers(name))));
			BodyMapper.copy(response.body().byteStream(), httpServletResponse.getOutputStream());
			return this;
		}
		httpServletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
		return this;
	}
}

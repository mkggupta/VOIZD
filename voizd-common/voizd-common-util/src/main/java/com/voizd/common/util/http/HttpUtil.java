/**
 * 
 */
package com.voizd.common.util.http;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author arvind
 *
 */
public class HttpUtil {
	public static final int GET = 1;
	public static final int POST = 2;
	public static final int HEAD = 3;
	public static final int OPTIONS = 4;
	public static final int PUT = 5;
	public static final int DELETE = 6;
	public static final int TRACE = 7;
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	protected static ThreadLocal<HttpClient> httpConn = new ThreadLocal<HttpClient>();

	protected static HttpClient getHttpClient() {
		HttpClient client = httpConn.get();
		if (client == null) {
			client = new HttpClient();
			httpConn.set(client);
		}

		return client;
	}

	public static byte[] accessURL(URL url, int socketTimeOut) throws Exception {
		return accessURL(url, GET, null, socketTimeOut);
	}

	public static byte[] accessURL(URL url, int type, byte[] payload, int socketTimeOut) throws Exception {
		return accessURL(url, type, HttpURLConnection.HTTP_OK, payload, socketTimeOut);
	}

	public static byte[] accessURL(URL url, int type, int expectedHttpCode, byte[] payload, int socketTimeOut) throws Exception {
		return accessURL(url, type, expectedHttpCode, null, payload, socketTimeOut);
	}

	public static byte[] accessURL(URL url, int type, int expectedHttpCode, Header[] hdrs, byte[] payload, int socketTimeOut) throws Exception {
		HttpMethodBase request = null;
		logger.info("accessURL():url = "+url+", type = "+type+", socketTimeOut = "+socketTimeOut);
		
		try {
			request = createMethod(url, type, payload);

//			request.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, new Integer(socketTimeOut));

			int hdrCount = hdrs != null ? hdrs.length : 0;
			for (int n = 0; n < hdrCount; n++)
				request.addRequestHeader(hdrs[n]);

			getHttpClient().getHttpConnectionManager().getParams().setSoTimeout(socketTimeOut);
			int responseCode = getHttpClient().executeMethod(request);
			String response = request.getStatusText();
			logger.info("accessURL():response code is "+ response);

			// Validate that we are seeing the requested response code
			if (responseCode != expectedHttpCode) {
				throw new Exception("accessURL():ErrorCode=" + responseCode);
			}
			
			logger.info("accessURL():returning response as"+ request.getResponseBody());
			return request.getResponseBody();
		} finally {
			if (request != null)
				request.releaseConnection();
		}
	}

	public static HttpMethodBase createMethod(URL url, int type, byte[] payload) {
		HttpMethodBase request = null;
		switch (type) {
		case GET:
			request = new GetMethod(url.toString());
			break;
		case POST:
			request = new PostMethod(url.toString());
			RequestEntity entity = new ByteArrayRequestEntity(payload, "application/octet-stream");

			((PostMethod) request).setRequestEntity(entity);
			break;
		case HEAD:
			request = new HeadMethod(url.toString());
			break;
		case OPTIONS:
			request = new OptionsMethod(url.toString());
			break;
		case PUT:
			request = new PutMethod(url.toString());
			break;
		case DELETE:
			request = new DeleteMethod(url.toString());
			break;
		case TRACE:
			request = new TraceMethod(url.toString());
			break;
		}
		return request;
	}

}

package com.voizd.search.utils;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

	public HttpGetWithEntity() {
		super();
	}

	public HttpGetWithEntity(URI uri) {
		super();
		setURI(uri);
	}

	public HttpGetWithEntity(String uri) {
		super();
		setURI(URI.create(uri));
	}
	
	@Override
	public String getMethod() {
		return Constants.GET.getValue();
	}

}

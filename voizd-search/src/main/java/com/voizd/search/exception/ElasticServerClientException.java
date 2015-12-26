package com.voizd.search.exception;

public class ElasticServerClientException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ElasticServerClientException(String message) {
		super(message);
	}

	public ElasticServerClientException(String message, Throwable cause) {
		super(message, cause);
	}
}

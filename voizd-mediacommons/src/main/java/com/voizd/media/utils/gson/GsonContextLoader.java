package com.voizd.media.utils.gson;

import com.google.gson.Gson;

/**
 * 
 * @author Vikrant Singh
 *
 */
public class GsonContextLoader {

	private static final Gson context = new Gson();

	private GsonContextLoader() {
	}

	public static Gson getGsonContext() {
		return context;
	}

}

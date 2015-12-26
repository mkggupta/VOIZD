package com.voizd.search.utils;

import com.google.gson.Gson;

public class GsonContextLoader {
	private static final Gson context = new Gson();

	private GsonContextLoader() {
	}

	public static Gson getGsonContext() {
		return context;
	}
}

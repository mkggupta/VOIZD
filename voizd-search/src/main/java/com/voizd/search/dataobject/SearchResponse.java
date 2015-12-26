package com.voizd.search.dataobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.gson.Gson;
import com.voizd.search.utils.GsonContextLoader;

public class SearchResponse {
	
	private static Logger logger = LoggerFactory.getLogger(SearchResponse.class);
	
	public Map getJsonMap() {
		return jsonMap;
	}
	public void setJsonMap(Map jsonMap) {
		this.jsonMap = jsonMap;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public String getPathToResult() {
		return pathToResult;
	}
	public void setPathToResult(String pathToResult) {
		this.pathToResult = pathToResult;
	}
	
	public boolean isSucceeded() {
		return isSucceeded;
	}
	public void setSucceeded(boolean isSucceeded) {
		this.isSucceeded = isSucceeded;
	}
	
	private Map jsonMap;
	private String jsonString;
    private String pathToResult;
	private boolean isSucceeded;

	public <T> T getSourceAsObject(Class<?> clazz) {
		List sourceList = ((List) extractSource());
		if (sourceList.size() > 0)
			return createSourceObject(sourceList.get(0), clazz);
		else
			return null;
	}

	private <T> T createSourceObject(Object source, Class<?> type) {
		Object obj = null;
		try {
			if (source instanceof Map) {
				Gson gson = GsonContextLoader.getGsonContext();
				String json = gson.toJson(source, Map.class);
				obj = gson.fromJson(json, type);
			} else {
				obj = type.cast(source);
			}

		} catch (Exception e) {
			logger.error("createSourceObject():: "+e.getLocalizedMessage(),e);
		}
		return (T) obj;
	}

	public Long getTotalCount() {
		Double count = 0.0;
		if (jsonMap != null) {
			try {
				Object obj = jsonMap.get("hits");
				count = (Double) ((Map) obj).get("total");
			} catch (Exception e) {
				logger.error("getTotalCount():: "+e.getLocalizedMessage(),e);	
			}
		}
		return count.longValue();
	}

	public <T> T getSourceAsObjectList(Class<?> type) {
		List<Object> objectList = new ArrayList<Object>();
		if (!isSucceeded)
			return (T) objectList;
		List<Object> sourceList = (List<Object>) extractSource();
		for (Object source : sourceList) {
			Object obj = createSourceObject(source, type);
			if (obj != null)
				objectList.add(obj);
		}
		return (T) objectList;
	}

	protected Object extractSource() {
		List<Object> sourceList = new ArrayList<Object>();
		
		if (jsonMap == null)
			return sourceList;
		String[] keys = getKeys();
		
		if (keys == null) {
			sourceList.add(jsonMap);
			return sourceList;
		}
		String sourceKey = keys[keys.length - 1];
		Object obj = jsonMap.get(keys[0]);
		if (keys.length > 1) {
			for (int i = 1; i < keys.length - 1; i++) {
				obj = ((Map) obj).get(keys[i]);
			}
			if (obj instanceof Map) {
				Map<String, Object> source = (Map<String, Object>) ((Map) obj)
						.get(sourceKey);
				if (source != null)
					sourceList.add(source);
			} else if (obj instanceof List) {
				for (Object newObj : (List) obj) {
					if (newObj instanceof Map) {
						Map<String, Object> source = (Map<String, Object>) ((Map) newObj)
								.get(sourceKey);
						if (source != null) {							
							sourceList.add(source);
						}
					}
				}
			}
		} else {
			if (obj != null) {
				sourceList.add(obj);
			}
		}
		return sourceList;
	}

	protected String[] getKeys() {
		return pathToResult == null ? null : (pathToResult + "").split("/");
	}
}

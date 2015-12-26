package com.voizd.common.util.resource;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtils {
	private static Logger logger = LoggerFactory.getLogger(ResourceUtils.class);
	 private static java.util.ResourceBundle systemConfigBundle;

	  static {
	    systemConfigBundle = ResourceBundle.getBundle("voizd");
	  }

	  public static java.lang.String getSystemConfigProperty(String key) {

	    if (null == key || key.length() == 0) {
	      throw new IllegalArgumentException(
	          "The key to perfom a lookup was found null/ empty @ ResourceUtils:getSystemConfigProperty");
	    }
	    return systemConfigBundle.getString(key);

	  }
}

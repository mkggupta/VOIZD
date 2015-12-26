/**
 * 
 */
package com.voizd.common.util;

/**
 * @author arvind
 *
 */
public enum VoizdCategory {
	  	USER("user"),
	    STATION("gallery"),
	    DEFAULT("default"),
	    STREAM("community"),
	    CONTENT("appstore");
	    
	    private VoizdCategory(String value ) {
	        this.value = value;
	      
	    }
	    private String value;
	    
	    public String getCategory() {
			return value;
		}
}

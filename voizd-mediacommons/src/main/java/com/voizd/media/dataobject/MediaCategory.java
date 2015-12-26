package com.voizd.media.dataobject;


import com.voizd.media.dataobject.exception.UnsupportedTypeException;

/**
 * User: shalabh
 * Date: 24/7/12
 * Time: 12:12 PM
 */
public enum MediaCategory {

    COMMUNITY("community", "A", "communityCache"),
    COMMUNITY_PROFILE("communityprofile", "B", "communityCache"),
    P2P("p2p", "C", "p2pCache"),
    ADVT("advt", "D", "advtCache"),
    USER("user", "E", "userCache"),
    CONTEST("contest", "F", "contestCache"),
    GALLLERY("gallery", "G", "galleryCache"),
    WEB("web", "H", "webCache"),
    CMS("cms", "I", "cmsCache"),
    DEFAULT_PIC("defaultpic", "J", "defaultCache"),
    DEFAULT("default", "K", "defaultCache"),
    APPSTORE("appstore", "L", "appStoreCache"),
    ALL("all", "M", ""),
    COMMENT("comment", "N", "commentCache");
    

    private String value;

    private String code;

    private String cacheBucket;

    private MediaCategory(String value, String code, String cacheBucket) {
        this.value = value;
        this.code = code;
        this.cacheBucket = cacheBucket;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getCacheBucket() {
        return cacheBucket;
    }

    public static MediaCategory getMediaCategoryByName(String category) throws UnsupportedTypeException {
        MediaCategory requiredMediaCategory = DEFAULT;
        if ((category != null) && (category.length() > 0)) {
            for (MediaCategory mediaCategory : MediaCategory.values()) {
                if (category.equalsIgnoreCase(mediaCategory.getValue())) {
                    requiredMediaCategory = mediaCategory;
                    break;
                }
            }
        }

        return requiredMediaCategory;
    }

    public static MediaCategory getMediaCategoryForCode(String categoryCode) throws UnsupportedTypeException {
        MediaCategory requiredMediaCategory = null;
        if ((categoryCode != null) && (categoryCode.length() > 0)) {
            for (MediaCategory mediaCategory : MediaCategory.values()) {
                if (categoryCode.equalsIgnoreCase(mediaCategory.getCode())) {
                    requiredMediaCategory = mediaCategory;
                    break;
                }
            }
        }
        if (requiredMediaCategory == null) {
            throw new UnsupportedTypeException("No media category can be found for " + categoryCode);
        }
        return requiredMediaCategory;
    }
    
    @Override
    public String toString() {
        return value ;
    }
}

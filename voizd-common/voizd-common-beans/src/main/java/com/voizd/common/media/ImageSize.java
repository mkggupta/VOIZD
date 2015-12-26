package com.voizd.common.media;



public enum ImageSize {
	JPEG_128x160("JPEG_128x160", "128x160"),
	 O_128x160("128x160", "128x160"),
	 O_120x120("120x120", "120x120"),
	JPEG_176x208("JPEG_176x208","176x208"), 
	O_176x208("176x208","176x208"), 
	JPEG_176x220("JPEG_176x220", "176x220"),
	O_176x220("176x220", "176x220"),
	JPEG_240x320("JPEG_240x320", "240x320"), 
	JPEG_120x120("120x120", "120x120"),
	JPEG_75x75("JPEG_75x75", "75x75"), 
	O_75x75("75x75", "75x75"),
	O_240x320("240x320", "240x320"), 
	JPEG320("JPEG320", "320x240"), 
	JPEG_208x208("JPEG_208x208", "208x208"), 
	O_208x208("208x208", "208x208"), 
	JPEG_352x416("JPEG_352x416", "352x416"), 
	O_352x416("352x416", "352x416"),
	JPEG_480x360("JPEG_480x360", "480x360"), 
	O_480x360("480x360", "480x360"),
	JPEG_50x50("JPEG_50x50", "50x50"),
	JPEG_30x30("JPEG_30x30", "30x30"),
	O_30x30("30x30", "30x30"),
	JPEG_35x35("JPEG_35x35", "35x35"),
	O_35x35("35x35", "35x35"),
	JPEG_130x130("JPEG_130x130", "130x130"),
	O_130x130("130x130", "130x130"),
	O_320x480("320x480", "320x480"),
	O_320x240("320x240", "320x240"),
	O_50x50("50x50", "50x50"),
	O_50x100("50x100", "50x100"),
	O_50x150("50x150", "50x150"),
	O_50x200("50x200", "50x200"),
	O_50x250("50x250", "50x250"),
	O_50x300("50x300", "50x300"),
	O_100x50("100x50", "100x50"),
	O_100x100("100x100", "100x100"),
	O_100x150("100x150", "100x150"),
	O_100x200("100x200", "100x200"),
	O_100x250("100x250", "100x250"),
	O_100x300("100x300", "100x300"),
	O_150x50("150x50", "150x50"),
	O_150x100("150x100", "150x100"),
	O_150x150("150x150", "150x150"),
	O_150x200("150x200", "150x200"),
	O_150x250("150x250", "150x250"),
	O_150x300("150x300", "150x300"),
	O_200x50("200x50", "200x50"),
	O_200x100("200x100", "200x100"),
	O_200x150("200x150", "200x150"),
	O_200x200("200x200", "200x200"),
	O_200x250("200x250", "200x250"),
	O_200x300("200x300", "200x300"),
	O_250x50("250x50", "250x50"),
	O_250x100("250x100", "250x100"),
	O_250x150("250x150", "250x150"),
	O_250x200("250x200", "250x200"),
	O_250x250("250x250", "250x250"),
	O_250x300("250x300", "250x300"),
	O_300x50("300x50", "300x50"),
	O_300x100("300x100", "300x100"),
	O_300x150("300x150", "300x150"),
	O_300x200("300x200", "300x200"),
	O_300x250("300x250", "300x250"),
	O_300x300("300x300", "300x300"),
	O_480x800("480x800", "480x800"),
	O_720x1280("720x1280", "720x1280"),
	DEFAULT("JPEG320", "320x240"),
	GIF("gif","gif"),
	ORIGINAL("original","original"),
	DEFAULTSTR("default" , "default"), 
	PREVIEW("preview", "preview"),
	VIDEO_PREVIEW_LARGE("VIDEO_PREVIEW_LARGE", "VIDEO_PREVIEW_LARGE"),
	VIDEO_PREVIEW_SMALL("VIDEO_PREVIEW_SMALL", "VIDEO_PREVIEW_SMALL");
	
	private String oldValue;

	private String newValue;

	private ImageSize(String oldValue, String newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public String getOldValue() {
		return oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public static ImageSize getImageSizeFromOldValue(String oldValue) {
		ImageSize imageSize = ImageSize.DEFAULT;
		if (oldValue != null) {
			for (ImageSize imgSize : ImageSize.values()) {
				if (imgSize.getOldValue().equalsIgnoreCase(oldValue)) {
					imageSize = imgSize;
				}
			}
		}
		return imageSize;
	}

	public static ImageSize getImageSizeFromNewValue(String newValue) {
		ImageSize imageSize = ImageSize.DEFAULT;
		if (newValue != null) {
			for (ImageSize imgSize : ImageSize.values()) {
				if (imgSize.getNewValue().equalsIgnoreCase(newValue)) {
					imageSize = imgSize;
				}
			}
		}
		return imageSize;
	}
}
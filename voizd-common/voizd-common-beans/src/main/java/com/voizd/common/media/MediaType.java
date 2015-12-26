package com.voizd.common.media;

public enum MediaType {
	AUDIO("audio", "A"), VIDEO("video", "V"), IMAGE("image", "I"), TEXT("text",
			"T"), ALL("all", "AL");

	private String value;

	private String code;

	public String getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}

	private MediaType(String value, String code) {
		this.value = value;
		this.code = code;
	}

	public static MediaType getMediaTypeFromCode(String code) {
		MediaType requiredMediaType = null;
		for (MediaType mediaType : values()) {
			if (code.equalsIgnoreCase(mediaType.getCode())) {
				requiredMediaType = mediaType;
				break;
			}
		}

		return requiredMediaType;
	}

	public static MediaType getMediaTypeFromValue(String type) {
		MediaType requiredMediaType = null;
		for (MediaType mediaType : values()) {
			if (type.equalsIgnoreCase(mediaType.getValue())) {
				requiredMediaType = mediaType;
				break;
			}
		}

		return requiredMediaType;
	}

	@Override
	public String toString() {
		return value;
	}
}

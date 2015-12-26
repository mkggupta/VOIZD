package com.voizd.common.media;

public enum MimeType {

	IMAGE_GIF(MediaType.IMAGE, "image/gif", "gif", "I1", "gif"), IMAGE_PNG(
			MediaType.IMAGE, "image/png", "png", "I2", "png"), IMAGE_JPG(
			MediaType.IMAGE, "image/jpg", "jpg", "I3", "jpg"), IMAGE_JPEG(
			MediaType.IMAGE, "image/jpeg", "jpeg", "I4", "jpeg"), IMAGE_TIFF(
			MediaType.IMAGE, "image/tiff", "tiff", "I5", "tiff"), IMAGE_BMP(
			MediaType.IMAGE, "image/bmp", "bmp", "I6", "tiff"),

	AUDIO_MP3(MediaType.AUDIO, "audio/mp3", "mp3", "A1", "mp3"), AUDIO_QCELP(
			MediaType.AUDIO, "audio/qcelp", "qcp", "A2", "qcelp"), AUDIO_CAF(
			MediaType.AUDIO, "audio/caff", "caf", "A3", "caf"), AUDIO_WAV(
			MediaType.AUDIO, "audio/wav", "wav", "A4", "wav"), AUDIO_AMR(
			MediaType.AUDIO, "audio/amr", "amr", "A5", "amr"), AUDIO_AU(
			MediaType.AUDIO, "audio/basic", "au", "A6", "basic"), AUDIO_MID(
			MediaType.AUDIO, "audio/mid", "mid", "A7", "mid"), AUDIO_ADPM(
			MediaType.AUDIO, "audio/adpcm", "adp", "A8", "adp"), AUDIO_AAAC(
			MediaType.AUDIO, "audio/x-aac", "aac", "A9", "aac"), AUDIO_AIFF(
			MediaType.AUDIO, "audio/x-aiff", "aif", "A10", "aif"), AUDIO_PCM(
			MediaType.AUDIO, "audio/pcm", "pcm", "A11", "pcm"), AUDIO_MPGA(
			MediaType.AUDIO, "audio/mpga", "mpga", "A12", "mp3"), AUDIO_CAFF(
			MediaType.AUDIO, "audio/caff", "caff", "A13", "caf"), AUDIO_WAV1(
			MediaType.AUDIO, "audio/x-wav", "wav", "A14", "wav"),

	VIDEO_MP4(MediaType.VIDEO, "video/mp4", "mp4", "V1", "mp4"), VIDEO_3GP(
			MediaType.VIDEO, "video/3gp", "3gp", "V2", "3gp"), VIDEO_FLV(
			MediaType.VIDEO, "video/flv", "flv", "V3", "flv"), VIDEO_PCM(
			MediaType.VIDEO, "pcm", "pcm", "V4", "pcm"), VIDEO_3GPP(
			MediaType.VIDEO, "video/3gpp", "3gp", "V5", "3gpp"), VIDEO_3GP2(
			MediaType.VIDEO, "video/3gpp2", "3gp3", "V6", "3gpp2"), VIDEO_AVI(
			MediaType.VIDEO, "video/x-msvideo", "avi", "V7", "avi"), VIDEO_MPEG(
			MediaType.VIDEO, "video/mpeg", "mpeg", "V8", "mpeg"), VIDEO_QT(
			MediaType.VIDEO, "video/quicktime", "qt", "V9", "qt"), VIDEO_MOV(
			MediaType.VIDEO, "video/quicktime", "mov", "V10", "mov");

	// audio/x-aac .aac
	// audio/x-aiff .aif

	// bmp image/bmp

	// image/jpeg jpe
	// image/jpeg jpeg
	// image/jpeg jpg
	// image/pipeg jfif
	// image/tiff tif
	// image/tiff tiff
	// image/x-cmu-raster ras
	// image/x-cmx cmx
	// image/x-icon ico
	// image/x-portable-anymap pnm
	// image/x-portable-bitmap pbm
	// image/x-portable-graymap pgm
	// image/x-portable-pixmap ppm
	// image/x-rgb rgb
	// image/x-xbitmap xbm
	// image/x-xpixmap xpm

	// audio/basic au
	// audio/basic snd
	// audio/mid mid
	// audio/mid rmi
	// audio/mpeg mp3
	// audio/x-aiff aif
	// audio/x-aiff aifc
	// audio/x-aiff aiff
	// audio/x-mpegurl m3u
	// audio/x-pn-realaudio ra
	// audio/x-pn-realaudio ram

	// video/x-msvideo .avi
	// video/mpeg mp2
	// video/mpeg mpa
	// video/mpeg mpe
	// video/mpeg mpeg
	// video/mpeg mpg
	// video/mpeg mpv2
	// video/quicktime mov
	// video/quicktime qt
	// video/x-la-asf lsf
	// video/x-la-asf lsx
	// video/x-ms-asf asf
	// video/x-ms-asf asr
	// video/x-ms-asf asx
	// video/x-msvideo avi
	// video/x-sgi-movie movie

	// 3GP video/3gpp .3gp Wikipedia: 3GP
	// 3GP2 video/3gpp2 .3g2 Wikipedia: 3G2
	// audio/adpcm .adp
	// audio/x-aac .aac
	// audio/x-aiff .aif

	// video/x-msvideo .avi
	// image/bmp .bmp
	private MediaType mediaType;

	private String mimeType;

	private String ext;

	private String code;

	private String defaultFileExtension;

	private MimeType(MediaType mediaType, String mimeType, String ext,
			String code, String defaultFileExtension) {
		this.mediaType = mediaType;
		this.mimeType = mimeType;
		this.ext = ext;
		this.code = code;
		this.defaultFileExtension = defaultFileExtension;
	}

	public String getDefaultFileExtension() {
		return defaultFileExtension;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getExt() {
		return ext;
	}

	public String getCode() {
		return code;
	}

	public static MimeType getMimeTypeForCode(String code) {
		MimeType requiredMimeType = null;
		for (MimeType mimeType : MimeType.values()) {
			if (code.equalsIgnoreCase(mimeType.getCode())) {
				requiredMimeType = mimeType;
				break;
			}
		}

		return requiredMimeType;
	}

	public static MimeType getMimeTypeForExtension(String extension) {
		MimeType requiredMimeType = null;
		for (MimeType mimeType : MimeType.values()) {
			if (extension.equalsIgnoreCase(mimeType.getExt())) {
				requiredMimeType = mimeType;
				break;
			}
		}

		return requiredMimeType;
	}

	public static MimeType getMimeTypeForReqMimeTypeStr(String mimeTypeReq) {
		MimeType requiredMimeType = null;
		for (MimeType mimeType : MimeType.values()) {
			if (mimeTypeReq.equalsIgnoreCase(mimeType.getMimeType())) {
				requiredMimeType = mimeType;
				break;
			}
		}

		return requiredMimeType;
	}
}

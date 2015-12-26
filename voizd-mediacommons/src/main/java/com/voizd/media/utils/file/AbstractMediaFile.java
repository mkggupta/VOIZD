package com.voizd.media.utils.file;

import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.file.MediaFile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * @author Vikrant Singh
 */
public abstract class AbstractMediaFile {

    protected static final String FILE_ID_SEPARATOR = "_";

    protected static final Pattern FILE_ID_PATTERN = Pattern
            .compile(FILE_ID_SEPARATOR);

    protected static final AtomicLong UNIQUE_ID_COUNTER = new AtomicLong(
            System.currentTimeMillis() * 1000);

    protected static final String REPOSITORY_PATH_ORIGINAL = "original";

    protected static final String REPOSITORY_PATH_CONVERTED = "converted";

    protected static final String FILE_PATH_SEPARATOR = System
            .getProperty("file.separator");

    protected static final int FILE_DIR_LEVELS = 2;

    protected static final int FILE_DIR_NAME_LENGTH = 2;

    public abstract MediaFile getMediaFile(String[] fileFileds) throws UnsupportedTypeException;

    public abstract String generateFileId(MediaFile mediaFile);

    public abstract String getMediaPath(String fileId, MediaFile mediaFile, String basePath,
                                        Map<Integer, String> pathParams, boolean isOriginal) throws NoSuchAlgorithmException;

    public String generateUniqueId() {
        String uniqueId = Long
                .toString(UNIQUE_ID_COUNTER.incrementAndGet(), 36);
        return uniqueId;
    }

    protected static String generateShaHashForFileId(String fileId)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(fileId.getBytes()));
    }

    protected static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

}

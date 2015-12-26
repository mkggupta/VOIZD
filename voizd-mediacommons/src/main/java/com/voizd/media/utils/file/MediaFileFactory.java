package com.voizd.media.utils.file;

import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.file.MediaFile;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Vikrant Singh
 */
public class MediaFileFactory {

    private static final String FILE_ID_SEPARATOR = "_";
    private static final Pattern FILE_ID_PATTERN = Pattern
            .compile(FILE_ID_SEPARATOR);

    private static final int VERSION_INDEX = 1;

    private static Map<Integer, AbstractMediaFile> mediaFileTypes = new HashMap<Integer, AbstractMediaFile>();

    static {
        mediaFileTypes.put(1, new MediaFileFirstVersion());
    }

    public static MediaFile getMediaFile(String fileId) throws UnsupportedTypeException {
        String[] fields = splitFileParams(fileId);
        if (fields.length < VERSION_INDEX + 1) {
            return null;
        }
        Integer version = Integer.parseInt(fields[VERSION_INDEX]);
        AbstractMediaFile abstractMediaFile = mediaFileTypes.get(version);

        if (abstractMediaFile != null) {
            return abstractMediaFile.getMediaFile(fields);
        }
        return null;
    }

    public static String generateFileId(MediaFile mediaFile) {
        AbstractMediaFile abstractMediaFile = mediaFileTypes.get(mediaFile
                .getVersion());

        if (abstractMediaFile != null) {
            return abstractMediaFile.generateFileId(mediaFile);
        }
        return null;
    }

    public static String getFilePath(String fileId, MediaFile mediaFile, String basePath,
                                     Map<Integer, String> pathParams, Integer version, boolean isOriginal)
            throws NoSuchAlgorithmException {
        AbstractMediaFile abstractMediaFile = mediaFileTypes.get(version);

        if (abstractMediaFile != null) {
            return abstractMediaFile.getMediaPath(fileId, mediaFile, basePath,
                    pathParams, isOriginal);
        }
        return null;
    }

    private static String[] splitFileParams(String fileId) {
        String[] fields = FILE_ID_PATTERN.split(fileId);
        return fields;
    }

}

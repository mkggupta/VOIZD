package com.voizd.media.utils.file;

import com.voizd.media.dataobject.MediaCategory;
import com.voizd.media.dataobject.MediaType;
import com.voizd.media.dataobject.MimeType;
import com.voizd.media.dataobject.exception.UnsupportedTypeException;
import com.voizd.media.dataobject.file.MediaFile;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author Vikrant Singh
 */
public class MediaFileFirstVersion extends AbstractMediaFile {

    private static final int APP_ID_INDEX = 0;
    private static final int VERSION_INDEX = 1;
    private static final int REPO_ID_INDEX = 2;
    private static final int MEDIA_CATEGORY_INDEX = 3;
    private static final int MEDIA_TYPE_INDEX = 4;
    private static final int MIME_TYPE_INDEX = 5;
    private static final int UNIQUE_ID_INDEX = 6;

    public MediaFile getMediaFile(String[] fileFileds) throws UnsupportedTypeException {    	
        if (fileFileds != null && fileFileds.length == (UNIQUE_ID_INDEX + 1)) {
            String appId = fileFileds[APP_ID_INDEX];
            String version = fileFileds[VERSION_INDEX];
            String repoId = fileFileds[REPO_ID_INDEX];
            String mediaCategory = fileFileds[MEDIA_CATEGORY_INDEX];
            String mediaType = fileFileds[MEDIA_TYPE_INDEX];
            String mimeType = fileFileds[MIME_TYPE_INDEX];
            return new MediaFile(Integer.parseInt(appId), Integer.parseInt(version), Integer.parseInt(repoId), MediaCategory.getMediaCategoryForCode(mediaCategory),
                    MediaType.getMediaTypeFromCode(mediaType), MimeType.getMimeTypeForCode(mimeType));
        }
        return null;
    }

    public String generateFileId(MediaFile mediaFile) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mediaFile.getAppId()).append(FILE_ID_SEPARATOR);
        stringBuilder.append(mediaFile.getVersion()).append(FILE_ID_SEPARATOR);
        stringBuilder.append(mediaFile.getRepoId()).append(FILE_ID_SEPARATOR);
        stringBuilder.append(mediaFile.getMediaCategory().getCode()).append(FILE_ID_SEPARATOR);
        stringBuilder.append(mediaFile.getMediaType().getCode()).append(FILE_ID_SEPARATOR);
        stringBuilder.append(mediaFile.getMimeType().getCode()).append(FILE_ID_SEPARATOR);
        stringBuilder.append(generateUniqueId());
        return stringBuilder.toString();
    }

    public String getMediaPath(String fileId, MediaFile mediaFile, String basePath,
                               Map<Integer, String> pathParams, boolean isOriginal) throws NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(basePath);
        stringBuilder.append(isOriginal == true ? REPOSITORY_PATH_ORIGINAL : REPOSITORY_PATH_CONVERTED).append(FILE_PATH_SEPARATOR);
        stringBuilder.append(mediaFile.getMediaType().getCode()).append(FILE_PATH_SEPARATOR);
        stringBuilder.append(mediaFile.getMediaCategory().getCode()).append(FILE_PATH_SEPARATOR);
        stringBuilder.append(mediaFile.getMimeType().getCode()).append(FILE_PATH_SEPARATOR);
        if (pathParams != null && pathParams.size() > 0) {
            for (int i = 1; i <= pathParams.size(); i++) {
                stringBuilder.append(pathParams.get(i)).append(FILE_PATH_SEPARATOR);
            }
        }
        String shaHash = generateShaHashForFileId(fileId);
        for (int index = 0; index < FILE_DIR_LEVELS; index++) {
            String dirName = shaHash.substring(index * FILE_DIR_NAME_LENGTH, (index + 1) * FILE_DIR_NAME_LENGTH);
            stringBuilder.append(dirName).append(FILE_PATH_SEPARATOR);
        }
        return stringBuilder.toString();
    }
}

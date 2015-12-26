package com.voizd.media.dataobject;

/**
 * @author Vikrant Singh
 */
public class MediaResponse {

    private String fileId;

    private String extension;

    private long contentLength;
    

    private transient String destinationFileFullPath = null;

    public String getDestinationFileFullPath() {
        return destinationFileFullPath;
    }

    public void setDestinationFileFullPath(String destinationFileFullPath) {
        this.destinationFileFullPath = destinationFileFullPath;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

}

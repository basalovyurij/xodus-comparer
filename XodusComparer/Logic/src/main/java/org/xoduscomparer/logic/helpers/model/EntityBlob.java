package org.xoduscomparer.logic.helpers.model;

/**
 *
 * @author yurij
 */
public class EntityBlob extends Named {
    private long blobSize;

    public EntityBlob() {
    }

    public EntityBlob(String name, long blobSize) {
        this.name = name;
        this.blobSize = blobSize;
    }

    public long getBlobSize() {
        return blobSize;
    }

    public void setBlobSize(long blobSize) {
        this.blobSize = blobSize;
    }
}

package org.xoduscomparer.logic.helpers.model;

import java.util.Objects;

/**
 *
 * @author yurij
 */
public class EntityBlob extends Named {
    private long blobSize;
    private byte[] data;

    public EntityBlob() {
    }

    public EntityBlob(String name, long blobSize) {
        this.name = name;
        this.blobSize = blobSize;
    }

    public EntityBlob(String name, byte[] data) {
        this(name, data.length);
        this.data = data;
    }
    
    public EntityBlob(String name, long blobSize, byte[] data) {
        this(name, blobSize);
        this.data = data;
    }

    
    public long getBlobSize() {
        return blobSize;
    }

    public void setBlobSize(long blobSize) {
        this.blobSize = blobSize;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + (int) (this.blobSize ^ (this.blobSize >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityBlob other = (EntityBlob) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return this.blobSize == other.blobSize;
    }
    
    
}

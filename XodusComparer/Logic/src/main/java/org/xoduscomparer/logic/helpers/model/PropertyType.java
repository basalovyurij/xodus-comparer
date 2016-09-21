package org.xoduscomparer.logic.helpers.model;

import java.util.Objects;

/**
 *
 * @author yurij
 */
public class PropertyType {

    private boolean readonly;
    private String clazz;
    private String displayName;

    public PropertyType() {
    }

    public PropertyType(boolean readonly, String clazz, String displayName) {
        this.readonly = readonly;
        this.clazz = clazz;
        this.displayName = displayName;
    }
    
    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.readonly ? 1 : 0);
        hash = 47 * hash + Objects.hashCode(this.clazz);
        hash = 47 * hash + Objects.hashCode(this.displayName);
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
        final PropertyType other = (PropertyType) obj;
        if (this.readonly != other.readonly) {
            return false;
        }
        if (!Objects.equals(this.clazz, other.clazz)) {
            return false;
        }
        if (!Objects.equals(this.displayName, other.displayName)) {
            return false;
        }
        return true;
    }
}

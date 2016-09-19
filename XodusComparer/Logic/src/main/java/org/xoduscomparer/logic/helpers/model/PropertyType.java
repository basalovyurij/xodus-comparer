package org.xoduscomparer.logic.helpers.model;

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
}

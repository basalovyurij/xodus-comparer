package org.xoduscomparer.logic.helpers.model;

import java.util.Objects;

/**
 *
 * @author yurij
 */
public class EntityLink extends Named {

    private int typeId;
    private String type;
    private String label;
    private long entityId;

    public EntityLink() {
    }

    public EntityLink(String name, int typeId, long entityId) {
        this.name = name;
        this.typeId = typeId;
        this.entityId = entityId;
    }

    public EntityLink(String name, int typeId, String type, String label, long entityId) {
        this.name = name;
        this.typeId = typeId;
        this.type = type;
        this.label = label;
        this.entityId = entityId;
    }    
    
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + this.typeId;
        hash = 97 * hash + (int) (this.entityId ^ (this.entityId >>> 32));
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
        final EntityLink other = (EntityLink) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.typeId != other.typeId) {
            return false;
        }
        return this.entityId == other.entityId;
    }
    
    
}

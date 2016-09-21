package org.xoduscomparer.logic.helpers.model;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author yurij
 */
public class EntityView extends BaseVO {
    private String type;
    private String label;
    private String typeId;
    private List<EntityProperty> properties;
    private List<EntityLink> links;
    private List<EntityBlob> blobs;

    public EntityView() {
    }

    public EntityView(String id, String type, String label, String typeId, List<EntityProperty> properties, List<EntityLink> links, List<EntityBlob> blobs) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.typeId = typeId;
        this.properties = properties;
        this.links = links;
        this.blobs = blobs;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<EntityProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<EntityProperty> properties) {
        this.properties = properties;
    }

    public List<EntityLink> getLinks() {
        return links;
    }

    public void setLinks(List<EntityLink> links) {
        this.links = links;
    }

    public List<EntityBlob> getBlobs() {
        return blobs;
    }

    public void setBlobs(List<EntityBlob> blobs) {
        this.blobs = blobs;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.type);
        hash = 53 * hash + Objects.hashCode(this.label);
        hash = 53 * hash + Objects.hashCode(this.typeId);
        hash = 53 * hash + Objects.hashCode(this.properties);
        hash = 53 * hash + Objects.hashCode(this.links);
        hash = 53 * hash + Objects.hashCode(this.blobs);
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
        final EntityView other = (EntityView) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        if (!Objects.equals(this.typeId, other.typeId)) {
            return false;
        }
        if (!Objects.equals(this.properties, other.properties)) {
            return false;
        }
        if (!Objects.equals(this.links, other.links)) {
            return false;
        }
        if (!Objects.equals(this.blobs, other.blobs)) {
            return false;
        }
        return true;
    }
    
    
}

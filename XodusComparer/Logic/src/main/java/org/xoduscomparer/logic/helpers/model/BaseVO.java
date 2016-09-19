package org.xoduscomparer.logic.helpers.model;

/**
 *
 * @author yurij
 */
public abstract  class BaseVO {

    protected String id;

    public BaseVO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

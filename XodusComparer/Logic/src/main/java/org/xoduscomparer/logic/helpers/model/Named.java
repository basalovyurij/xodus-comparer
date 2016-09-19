package org.xoduscomparer.logic.helpers.model;

/**
 *
 * @author yurij
 */
public abstract class Named {

    protected String name;

    public Named() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

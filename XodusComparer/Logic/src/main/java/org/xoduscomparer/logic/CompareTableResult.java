package org.xoduscomparer.logic;

import java.util.Map;

/**
 *
 * @author yurij
 */
public class CompareTableResult {
    
    private CompareState state;    
    private Map<String, CompareObjectResult> objects;

    public CompareTableResult() {
    }

    public CompareState getState() {
        return state;
    }

    public void setState(CompareState state) {
        this.state = state;
    }
    
    public Map<String, CompareObjectResult> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, CompareObjectResult> objects) {
        this.objects = objects;
    }
}

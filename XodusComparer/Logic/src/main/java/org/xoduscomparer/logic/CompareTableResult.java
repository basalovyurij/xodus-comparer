package org.xoduscomparer.logic;

import java.util.List;

/**
 *
 * @author yurij
 */
public class CompareTableResult {
    
    private CompareState state;    
    private List<CompareObjectResult> objects;

    public CompareTableResult() {
    }

    public CompareState getState() {
        return state;
    }

    public void setState(CompareState state) {
        this.state = state;
    }
    
    public List<CompareObjectResult> getObjects() {
        return objects;
    }

    public void setObjects(List<CompareObjectResult> objects) {
        this.objects = objects;
    }
}

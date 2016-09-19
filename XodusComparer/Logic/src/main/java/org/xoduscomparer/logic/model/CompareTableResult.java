package org.xoduscomparer.logic.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author yurij
 */
public class CompareTableResult {

    private CompareState state;
    private Map<Long, CompareObjectResult> objects;

    public CompareTableResult() {
    }

    public CompareTableResult(CompareState state, Map<Long, CompareObjectResult> objects) {
        this.state = state;
        this.objects = objects;
    }
    
    public CompareState getState() {
        return state;
    }

    public void setState(CompareState state) {
        this.state = state;
    }

    public Map<Long, CompareObjectResult> getObjects() {
        return objects;
    }

    public void setObjects(Map<Long, CompareObjectResult> objects) {
        this.objects = objects;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.state);
        hash = 79 * hash + Objects.hashCode(this.objects);
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
        final CompareTableResult other = (CompareTableResult) obj;
        if (this.state != other.state) {
            return false;
        }
        return Objects.equals(this.objects, other.objects);
    }

}

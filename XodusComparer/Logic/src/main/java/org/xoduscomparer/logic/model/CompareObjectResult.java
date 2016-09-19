package org.xoduscomparer.logic.model;

import java.util.Objects;

/**
 *
 * @author yurij
 */
public class CompareObjectResult {
    
    private CompareState state;
    private Object value;
    private Object value2;

    public CompareObjectResult() {
    }

    public CompareObjectResult(CompareState state, Object value) {
        this(state, value, null);
    }
    
    public CompareObjectResult(CompareState state, Object value, Object value2) {
        this.state = state;
        this.value = value;
        this.value2 = value2;
    }

    public CompareState getState() {
        return state;
    }

    public void setState(CompareState state) {
        this.state = state;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.state);
        hash = 13 * hash + Objects.hashCode(this.value);
        hash = 13 * hash + Objects.hashCode(this.value2);
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
        final CompareObjectResult other = (CompareObjectResult) obj;
        if (this.state != other.state) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return Objects.equals(this.value2, other.value2);
    }
}

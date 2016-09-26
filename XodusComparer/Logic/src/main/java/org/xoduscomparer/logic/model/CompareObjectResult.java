package org.xoduscomparer.logic.model;

import java.util.Objects;
import org.xoduscomparer.logic.helpers.model.EntityView;

/**
 *
 * @author yurij
 */
public class CompareObjectResult {
    
    private CompareState state;
    private String label;
    private EntityView value;
    private EntityView value2;

    public CompareObjectResult() {
    }

    public CompareObjectResult(CompareState state, EntityView value) {
        this(state, value, null);
    }
    
    public CompareObjectResult(CompareState state, EntityView value, EntityView value2) {
        this.state = state;
        this.label = value.getLabel();
        this.value = value;
        this.value2 = value2;
    }

    public CompareState getState() {
        return state;
    }

    public void setState(CompareState state) {
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public Object getValue() {
        return value;
    }

    public void setValue(EntityView value) {
        this.value = value;
        this.label = value.getLabel();
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(EntityView value2) {
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

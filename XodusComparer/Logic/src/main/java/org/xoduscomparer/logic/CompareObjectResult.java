package org.xoduscomparer.logic;

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
}

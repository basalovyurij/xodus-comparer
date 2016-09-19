package org.xoduscomparer.logic.model;

/**
 *
 * @author yurij
 */
public enum CompareState {
    
    EXIST_BOTH(0),
    EXIST_BOTH_EQUAL(1),
    EXIST_BOTH_DIFF(2),
    EXIST_ONLY_FIRST(3),
    EXIST_ONLY_SECOND(4);
    
    private final int value;

    private CompareState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

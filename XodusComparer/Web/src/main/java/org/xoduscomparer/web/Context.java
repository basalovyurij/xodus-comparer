package org.xoduscomparer.web;

import org.xoduscomparer.logic.model.CompareDbResult;

/**
 *
 * @author yurij
 */
public class Context {

    private static volatile Context instance;

    public static Context getInstance() {
        Context localInstance = instance;
        if (localInstance == null) {
            synchronized (Context.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Context();
                }
            }
        }
        return localInstance;
    }

    private CompareDbResult compareDbResult;
    
    private Context() {
    }

    public CompareDbResult getCompareDbResult() {
        return compareDbResult;
    }

    public void setCompareDbResult(CompareDbResult compareDbResult) {
        this.compareDbResult = compareDbResult;
    }
}

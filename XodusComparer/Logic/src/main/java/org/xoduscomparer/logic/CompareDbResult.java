package org.xoduscomparer.logic;

import java.util.Map;

/**
 *
 * @author yurij
 */
public class CompareDbResult {

    private Map<String, CompareTableResult> tables;

    public CompareDbResult() {
    }

    public Map<String, CompareTableResult> getTables() {
        return tables;
    }

    public void setTables(Map<String, CompareTableResult> tables) {
        this.tables = tables;
    }
}

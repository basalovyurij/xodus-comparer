package org.xoduscomparer.logic.model;

import java.util.Map;
import java.util.Objects;

/**
 *
 * @author yurij
 */
public class CompareDbResult {

    private Map<String, CompareTableResult> tables;

    public CompareDbResult() {
    }

    public CompareDbResult(Map<String, CompareTableResult> tables) {
        this.tables = tables;
    }

    public Map<String, CompareTableResult> getTables() {
        return tables;
    }

    public void setTables(Map<String, CompareTableResult> tables) {
        this.tables = tables;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.tables);
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
        final CompareDbResult other = (CompareDbResult) obj;
        return Objects.equals(this.tables, other.tables);
    }
}

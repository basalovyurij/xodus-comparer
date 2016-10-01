package org.xoduscomparer.logic.model;

import java.util.Map;
import java.util.Objects;

/**
 *
 * @author yurij
 */
public class CompareDbResult {

    private String dbPath1;
    private String dbPath2;
    private String savePath;
    private Long size;
    private Map<String, CompareTableResult> tables;

    public CompareDbResult() {
    }

    public CompareDbResult(Map<String, CompareTableResult> tables) {
        this.tables = tables;
    }

    public String getDbPath1() {
        return dbPath1;
    }

    public void setDbPath1(String dbPath1) {
        this.dbPath1 = dbPath1;
    }

    public String getDbPath2() {
        return dbPath2;
    }

    public void setDbPath2(String dbPath2) {
        this.dbPath2 = dbPath2;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
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
        hash = 67 * hash + Objects.hashCode(this.tables);
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

package org.xoduscomparer.logic;

import org.xoduscomparer.logic.model.CompareDbResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author yurij
 */
public class CompareResultManager {

    public static CompareDbResult load(String path) throws IOException {
        CompareDbResult cmp = JSON.parseObject(new String(Files.readAllBytes(Paths.get(path))), CompareDbResult.class);

        System.gc();

        cmp.setSavePath(path);
        cmp.setSize(new File(path).length());

        return cmp;
    }

    public static void save(String path, CompareDbResult cmp) throws IOException {
        cmp.setSavePath(null);
        cmp.setSize(null);

        try (JSONWriter writer = new JSONWriter(new FileWriter(new File(path)))) {
            writer.startObject();
            
            writer.writeKey("dbPath1");
            writer.writeValue(cmp.getDbPath1());
            
            writer.writeKey("dbPath1");
            writer.writeValue(cmp.getDbPath1());
            
            writer.writeKey("tables");
            writer.startObject();
            cmp.getTables().entrySet().stream().forEach(e -> {
                writer.writeKey(e.getKey());
                writer.writeValue(e.getValue());
            });
            writer.endObject();
            
            writer.endObject();
        }

        System.gc();

        if (cmp.getSavePath() == null) {
            cmp.setSavePath(path);
            cmp.setSize(new File(path).length());
        }
    }
}

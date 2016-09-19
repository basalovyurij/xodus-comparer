package org.xoduscomparer.logic;

import org.xoduscomparer.logic.model.CompareDbResult;
import com.alibaba.fastjson.JSON;
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

    public CompareDbResult load(String path) throws IOException {
        return JSON.parseObject(new String(Files.readAllBytes(Paths.get(path))), CompareDbResult.class);
    }

    public void save(String path, CompareDbResult db) throws IOException {
        try (FileWriter writer = new FileWriter(new File(path))) {
            writer.write(JSON.toJSONString(db));
        }
    }
}

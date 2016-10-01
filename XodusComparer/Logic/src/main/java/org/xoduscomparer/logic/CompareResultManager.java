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

    public static CompareDbResult load(String path) throws IOException {
        CompareDbResult cmp = JSON.parseObject(new String(Files.readAllBytes(Paths.get(path))), CompareDbResult.class);
        
        cmp.setSavePath(path);
        cmp.setSize(new File(path).length());
        
        return cmp;
    }

    public static void save(String path, CompareDbResult cmp) throws IOException {
        cmp.setSavePath(null);
        cmp.setSize(null);
        
        try (FileWriter writer = new FileWriter(new File(path))) {
            writer.write(JSON.toJSONString(cmp));
        }
        
        if(cmp.getSavePath() == null) {
            cmp.setSavePath(path);
            cmp.setSize(new File(path).length());
        }
    }
}

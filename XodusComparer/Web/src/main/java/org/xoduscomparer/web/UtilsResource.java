package org.xoduscomparer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import org.xoduscomparer.logic.CompareDb;
import org.xoduscomparer.logic.CompareResultManager;
import org.xoduscomparer.logic.model.CompareDbResult;
import spark.Request;
import spark.Response;
import static spark.Spark.post;

public class UtilsResource extends BaseResource {

    private static final String END_POINT = API_CONTEXT + "/utils";

    public UtilsResource() {
        super();

        post(END_POINT + "/status", "application/json", wrap((req, resp) -> status(req, resp)), new JsonTransformer());
        post(END_POINT + "/compare", "application/json", wrap((req, resp) -> compare(req, resp)), new JsonTransformer());
        post(END_POINT + "/save", "application/json", wrap((req, resp) -> save(req, resp)), new JsonTransformer());
        post(END_POINT + "/load", "application/json", wrap((req, resp) -> load(req, resp)), new JsonTransformer());
        post(END_POINT + "/filesystem", "application/json", wrap((req, resp) -> filesystem(req, resp)), new JsonTransformer());
    }

    private Object status(Request request, Response response) {
        JSONObject obj = new JSONObject();

        obj.put("directorySeparator", File.separator);

        response.status(200);

        CompareDbResult cmp = Context.getInstance().getCompareDbResult();
        if (cmp != null) {
            obj.put("inited", true);
            obj.put("dbPath1", cmp.getDbPath1());
            obj.put("dbPath2", cmp.getDbPath2());
            obj.put("savePath", cmp.getSavePath());
            obj.put("size", cmp.getSize());
        }


        return obj;
    }

    private Object compare(Request request, Response response) {
        JSONObject obj = JSON.parseObject(request.body());

        File f1 = new File(obj.getString("db1"));
        if(!f1.exists() || !f1.isDirectory()) {
            return validationErrors(response, Arrays.asList("Путь к певой БД не существует или не валиден"));
        }
        
        File f2 = new File(obj.getString("db2"));
        if(!f2.exists() || !f2.isDirectory()) {
            return validationErrors(response, Arrays.asList("Путь ко второй БД не существует или не валиден"));
        }
        
        if(obj.getString("db1").equalsIgnoreCase(obj.getString("db2"))) {
            return validationErrors(response, Arrays.asList("Пути к сравнивыемым БД должны быть различными"));
        }
        
        CompareDb cmp = new CompareDb(obj.getString("db1"), obj.getString("key1"), obj.getString("db2"), obj.getString("key2"));
        Context.getInstance().setCompareDbResult(cmp.compare());

        response.status(200);
        return response;
    }

    private Object save(Request request, Response response) throws IOException {
        JSONObject obj = JSON.parseObject(request.body());
        String path = obj.getString("path");

        logger.info(String.format("Start save comprasion to [%s]", path));
        CompareDbResult cmp = Context.getInstance().getCompareDbResult();
        logger.info(String.format("Successuful save comprasion to [%s]", path));

        CompareResultManager.save(path, cmp);
        
        return response;
    }

    private Object load(Request request, Response response) throws IOException {
        JSONObject obj = JSON.parseObject(request.body());
        String path = obj.getString("path");

        if(!new File(path).exists()) {
            return validationErrors(response, Arrays.asList("Файл не существует"));
        }
        
        logger.info(String.format("Start load comprasion from [%s]", path));
        CompareDbResult cmp = CompareResultManager.load(path);
        logger.info(String.format("Successuful load comprasion from [%s]", path));

        Context.getInstance().setCompareDbResult(cmp);

        return response;
    }

    private Object filesystem(Request request, Response response) {
        String path = null;
        if (request.body() != null && !request.body().isEmpty()) {
            JSONObject obj = JSON.parseObject(request.body());
            path = obj.getString("path");
        }

        File[] files;
        if (path == null || path.isEmpty()) {
            files = File.listRoots();
        } else {
            files = new File(path).listFiles();
        }

        JSONArray fileList = new JSONArray();
        if (files != null) {
            Arrays.asList(files)
                    .stream()
                    .sorted((a, b) -> a.getName().compareTo(b.getName()))
                    .forEach(file -> fileList.add(getInfo(file)));
        }

        return fileList;
    }

    private JSONObject getInfo(File file) {
        JSONObject obj = new JSONObject();

        obj.put("folder", file.isDirectory());
        obj.put("name", file.getName().isEmpty() ? "/" : file.getName());
        if (!file.isDirectory()) {
            obj.put("size", formatFileSize(file.length()));
        }
        obj.put("type", "");
        obj.put("dateModified", new Date(file.lastModified()));

        if (file.isDirectory()) {
            obj.put("children", new JSONArray());
        }

        return obj;
    }

    private String formatFileSize(Long bytes) {
        String[] units = new String[]{"B", "КB", "MB", "GB", "TB", "PB"};
        Double number = bytes == 0L ? 0 : Math.floor(Math.log(bytes) / Math.log(1024));
        return String.format("%.2f %s", bytes.doubleValue() / Math.pow(1024, Math.floor(number)), units[number.intValue()]);
    }
}

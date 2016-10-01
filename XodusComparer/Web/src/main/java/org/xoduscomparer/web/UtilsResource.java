package org.xoduscomparer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        
        post(END_POINT + "/status", "application/json", (req, resp) -> status(req, resp), new JsonTransformer());
        post(END_POINT + "/compare", "application/json", (req, resp) -> compare(req, resp), new JsonTransformer());
        post(END_POINT + "/save", "application/json", (req, resp) -> save(req, resp), new JsonTransformer());
        post(END_POINT + "/load", "application/json", (req, resp) -> load(req, resp), new JsonTransformer());
    }

    private Object status(Request request, Response response) {
        JSONObject obj = new JSONObject();

        response.status(200);
        
        CompareDbResult cmp = Context.getInstance().getCompareDbResult();
        if(cmp != null) {
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

        CompareDb cmp = new CompareDb(obj.getString("db1"), obj.getString("key1"), obj.getString("db2"), obj.getString("key2"));
        Context.getInstance().setCompareDbResult(cmp.compare());

        response.status(200);
        return response;
    }

    private Object save(Request request, Response response) {
        try {
            JSONObject obj = JSON.parseObject(request.body());
            String path = obj.getString("path");

            logger.info(String.format("Start save comprasion to [%s]", path));
            CompareDbResult cmp = Context.getInstance().getCompareDbResult();
            logger.info(String.format("Successuful save comprasion to [%s]", path));
        
            CompareResultManager.save(path, cmp);
            response.status(200);
        } catch (Exception e) {
            response.status(500);
            logger.error("", e);
        }
        return response;
    }

    private Object load(Request request, Response response) {
        try {
            JSONObject obj = JSON.parseObject(request.body());
            String path = obj.getString("path");

            logger.info(String.format("Start load comprasion from [%s]", path));
            CompareDbResult cmp = CompareResultManager.load(path);
            logger.info(String.format("Successuful load comprasion from [%s]", path));
        
            Context.getInstance().setCompareDbResult(cmp);
            
            response.status(200);
        } catch (Exception e) {
            response.status(500);
            logger.error("", e);
        }
        return response;
    }
}

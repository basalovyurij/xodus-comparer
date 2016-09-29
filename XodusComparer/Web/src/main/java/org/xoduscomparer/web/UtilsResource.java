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
        
        post(END_POINT + "/compare", "application/json", (req, resp) -> compare(req, resp), new JsonTransformer());
        post(END_POINT + "/save", "application/json", (req, resp) -> save(req, resp), new JsonTransformer());
        post(END_POINT + "/load", "application/json", (req, resp) -> load(req, resp), new JsonTransformer());
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

            CompareDbResult cmp = Context.getInstance().getCompareDbResult();

            CompareResultManager.save(path, cmp);
            response.status(200);
            logger.info(String.format("Successuful save comprasion to [%s]", path));
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

            CompareDbResult cmp = CompareResultManager.load(path);
            
            Context.getInstance().setCompareDbResult(cmp);
            
            response.status(200);
            logger.info(String.format("Successuful load comprasion from [%s]", path));
        } catch (Exception e) {
            response.status(500);
            logger.error("", e);
        }
        return response;
    }
}

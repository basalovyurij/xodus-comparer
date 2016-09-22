package org.xoduscomparer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.xoduscomparer.logic.CompareDb;
import org.xoduscomparer.logic.model.CompareDbResult;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class UtilsResource {

    private static final String API_CONTEXT = "/api/v1";

    //private final TodoService todoService;

    public UtilsResource() {
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(API_CONTEXT + "/utils", "application/json", (request, response) -> {
            JSONObject obj = JSON.parseObject(request.body());
            
            CompareDb cmp = new CompareDb(obj.getString("db1"), obj.getString("db2"), obj.getString("key"));
            Context.getInstance().setCompareDbResult(cmp.compare());
                        
            response.status(200);
            return response;
        }, new JsonTransformer());
    }

}

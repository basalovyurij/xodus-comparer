package org.xoduscomparer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.xoduscomparer.logic.CompareDb;
import spark.Request;
import spark.Response;
import static spark.Spark.post;

public class UtilsResource extends BaseResource {

    private static final String END_POINT = API_CONTEXT + "/utils";

    public UtilsResource() {
        post(END_POINT + "/compare", "application/json", (req, resp) ->  compare(req, resp), new JsonTransformer());
    }

    private Object compare(Request request, Response response) {
        JSONObject obj = JSON.parseObject(request.body());
            
        CompareDb cmp = new CompareDb(obj.getString("db1"), obj.getString("db2"), obj.getString("key"));
        Context.getInstance().setCompareDbResult(cmp.compare());

        response.status(200);
        return response;
    }
}

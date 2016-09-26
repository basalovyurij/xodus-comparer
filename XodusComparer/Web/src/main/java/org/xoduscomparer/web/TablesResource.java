package org.xoduscomparer.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.xoduscomparer.logic.model.CompareTableResult;
import spark.Request;
import spark.Response;
import static spark.Spark.get;

public class TablesResource extends BaseResource {

    private static final String END_POINT = API_CONTEXT + "/tables";

    public TablesResource() {
        get(END_POINT, "application/json", (req, resp) -> getTables(req, resp), new JsonTransformer());
        get(END_POINT + "/:name", "application/json", (req, resp) -> getTable(req, resp), new JsonTransformer());
    }

    private Object getTables(Request request, Response response) {
        JSONArray res = new JSONArray();

        Context.getInstance().getCompareDbResult().getTables().entrySet().forEach(e -> {
            JSONObject obj = new JSONObject();

            obj.put("name", e.getKey());
            obj.put("state", e.getValue().getState().getDescription());

            res.add(obj);
        });

        response.status(200);

        return res;
    }

    private Object getTable(Request request, Response response) {
        String name = request.params(":name");

        CompareTableResult info = Context.getInstance().getCompareDbResult().getTables().get(name);
        
        JSONObject res = new JSONObject();
        
        res.put("name", name);
        res.put("objCount", info.getObjects().size());

        response.status(200);

        return res;
    }
}

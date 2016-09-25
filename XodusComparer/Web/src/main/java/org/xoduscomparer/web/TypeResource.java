package org.xoduscomparer.web;

import com.alibaba.fastjson.JSONArray;
import org.xoduscomparer.logic.model.CompareTableResult;
import spark.Request;
import spark.Response;
import static spark.Spark.get;

public class TypeResource extends BaseResource {

    private static final String END_POINT = API_CONTEXT + "/type";

    public TypeResource() {
        get(END_POINT + "/:name/entities", "application/json", (req, resp) -> getEntities(req, resp), new JsonTransformer());
    }

    private Object getEntities(Request request, Response response) {
        String name = request.params(":name");
        int offset = Integer.parseInt(request.queryParams("offset"));
        int pageSize = Integer.parseInt(request.queryParams("pageSize"));
        
        CompareTableResult item = Context.getInstance().getCompareDbResult().getTables().get(name);
                
        JSONArray res = new JSONArray();

        item.getObjects().entrySet().stream().skip(offset).limit(pageSize).forEach(e -> {
            res.add(e.getValue());
        });

        response.status(200);

        return res;
    }
}

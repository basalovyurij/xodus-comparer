package org.xoduscomparer.web;

import com.alibaba.fastjson.JSON;
import spark.Response;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        if (model instanceof Response) {
            return "";
        }
        return JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm");
    }

}
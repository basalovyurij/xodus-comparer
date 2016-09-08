package org.xoduscomparer.web;

import com.alibaba.fastjson.JSON;
import spark.Response;
import spark.ResponseTransformer;

import java.util.HashMap;

public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        if (model instanceof Response) {
            return JSON.toJSONString(new HashMap<>());
        }
        return JSON.toJSONString(model);
    }

}
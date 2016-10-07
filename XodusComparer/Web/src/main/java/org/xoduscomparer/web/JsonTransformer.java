package org.xoduscomparer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import java.io.IOException;
import java.lang.reflect.Type;
import org.xoduscomparer.logic.model.CompareState;
import spark.Response;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private static final SerializeConfig config = new SerializeConfig();

    static {
        config.put(CompareState.class, new CompareStateSerializer());
    }

    @Override
    public String render(Object model) {
        if (model instanceof Response) {
            return "";
        }
        return JSON.toJSONString(model, config, null, "yyyy-MM-dd HH:mm", JSON.DEFAULT_GENERATE_FEATURE);
    }

    private static class CompareStateSerializer implements ObjectSerializer {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            CompareState value = (CompareState) object;
            if (value == null) {
                serializer.write("");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("value", value.getValue());
            json.put("description", value.getDescription());
            serializer.write(json);
        }

    }
}

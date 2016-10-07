package org.xoduscomparer.logic.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 *
 * @author yurij
 */
public enum CompareState {
    
    EXIST_BOTH(0, "Присутствует в обоих"),
    EXIST_BOTH_EQUAL(1, "Присутствует в обоих и равны"),
    EXIST_BOTH_DIFF(2, "Присутствует в обоих и отличаются"),
    EXIST_ONLY_FIRST(3, "Присутствует только в первом"),
    EXIST_ONLY_SECOND(4, "Присутствует только во втором");
    
    private final int value;
    private final String description;

    private CompareState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }   
}

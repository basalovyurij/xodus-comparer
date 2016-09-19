package org.xoduscomparer.logic.helpers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 *
 * @author yurij
 */
public class UIPropertyTypes {

    private static final Map<Class, UIPropertyType> BY_CLASS = new ConcurrentHashMap<>();
    private static final Map<String, UIPropertyType> BY_NAME = new ConcurrentHashMap<>();

    private static final UIPropertyType STRING = newType(String.class, t -> t);
    private static final UIPropertyType BOOLEAN = newType(Boolean.class, t -> Boolean.valueOf(t));
    private static final UIPropertyType BYTE = newType(Byte.class, t -> Byte.valueOf(t));
    private static final UIPropertyType SHORT = newType(Short.class, t -> Short.valueOf(t));
    private static final UIPropertyType INT = newType(Integer.class, t -> Integer.valueOf(t));
    private static final UIPropertyType LONG = newType(Long.class, t -> Long.valueOf(t));
    private static final UIPropertyType FLOAT = newType(Float.class, t -> Float.valueOf(t));
    private static final UIPropertyType DOUBLE = newType(Double.class, t -> Double.valueOf(t));
    
    private static <T extends Comparable> UIPropertyType<T> newType(Class clazz, Function<String, T> function) {
        String fullName = clazz.getTypeName();
        UIPropertyType<T> type = new UIPropertyType<>(fullName, function);
        BY_CLASS.put(clazz, type);
        BY_NAME.put(fullName, type);
        return type;
    }

    public static Boolean isSupported(Class clazz) {
        return BY_CLASS.containsKey(clazz);
    }
    
    public static <T extends Comparable> UIPropertyType<T> uiTypeOf(Class clazz) {
        return BY_CLASS.get(clazz);
    }

    public static <T extends Comparable> UIPropertyType<T>  uiTypeOf(String clazz) {
        return BY_NAME.get(clazz);
    }
    
    public static class UIPropertyType<T extends Comparable> {
        
        private final String clazz;
        private final Function<String, T> function;

        private UIPropertyType(String clazz, Function<String, T> function) {
            this.clazz = clazz;
            this.function = function;
        }

        public String toString(T value) {
            if (value == null) {
                return null;
            }
            return value.toString();
        }

        public T toValue(String value) {
            if (value == null) {
                return null;
            }
            return function.apply(value);
        }

        public Boolean isValid(String value) {
            try {
                function.apply(value);
                return true;
            } catch (RuntimeException e) {
                // ignore result if conversion failed
                return false;
            }
        }
    }
}

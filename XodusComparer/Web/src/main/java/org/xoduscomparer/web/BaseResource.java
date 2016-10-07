package org.xoduscomparer.web;

import com.alibaba.fastjson.JSONObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.BiFunction;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author yurij
 */
public class BaseResource {

    protected static final String API_CONTEXT = "/api/v1";

    protected final Logger logger;

    protected BaseResource() {
        logger = LogManager.getLogger(getClass());
    }

    protected final Route wrap(CheckedFunction action) {
        return (Request request, Response response) -> {
            try {
                response.status(200);
                return action.apply(request, response);
            } catch (Exception e) {
                response.status(500);
                logger.error("", e);

                JSONObject exception = new JSONObject();

                exception.put("message", e.toString());
                exception.put("stacktrace", getStackTrace(e));

                return exception;
            }
        };
    }

    private final String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
    
    @FunctionalInterface
    public static interface CheckedFunction {
       Object apply(Request request, Response response) throws Exception;
    }
}

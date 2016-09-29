package org.xoduscomparer.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
}

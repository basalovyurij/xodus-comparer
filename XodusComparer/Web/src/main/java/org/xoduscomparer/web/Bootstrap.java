package org.xoduscomparer.web;

import static spark.Spark.setIpAddress;
import static spark.Spark.setPort;
import static spark.SparkBase.staticFileLocation;

public class Bootstrap {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        staticFileLocation("/public");
        
        new UtilsResource();
    }
}

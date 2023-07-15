package com.illoivan.duplicatefiles.utils;

import org.apache.logging.log4j.LogManager;	
import org.apache.logging.log4j.Logger;

public class LoggerUtils {
	private static Logger logger;
	private static final String FORMAT_STR = "{}: {}";
	
    private LoggerUtils() {}

    static {
        logger = LogManager.getLogger(LoggerUtils.class);
    }

    public static void info(String message) {
        String caller = getCallerInfo();
        logger.info(FORMAT_STR, caller, message);
    }

    public static void debug(String message) {
        String caller = getCallerInfo();
        logger.debug(FORMAT_STR, caller, message);
    }

    public static void error(String message, Throwable throwable) {
        String caller = getCallerInfo();
        logger.error(FORMAT_STR, caller, message, throwable);
    }
    
    public static void error(String message) {
        String caller = getCallerInfo();
        logger.error(FORMAT_STR, caller, message);
    }

    private static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3];
        String className = caller.getClassName().substring(caller.getClassName().lastIndexOf(".") + 1);
        String methodName = caller.getMethodName();
        int line = caller.getLineNumber();
        return String.format("%s-%s-[%s]",className,methodName,line);
    }

}

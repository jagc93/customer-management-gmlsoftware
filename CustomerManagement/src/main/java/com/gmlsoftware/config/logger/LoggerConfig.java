package com.gmlsoftware.config.logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.gmlsoftware.service.UseLogger;

public class LoggerConfig {

	private static final Map<Class<?>, Logger> loggerMap = new HashMap<>();

	{
		scanAndInitializeLoggers("com.gmlsoftware");
	}

    public static <T> Logger logger(Class<T> clazz) {
        return getLoggerForClass(clazz);
    }

    public static void info(Logger logger, String message) {
    	MDC.put("threadId", Long.toString(Thread.currentThread().getId()));
    	logger.info(message);
    	MDC.remove("threadId");
    }

    public static void error(Logger logger, String message) {
    	MDC.put("threadId", Long.toString(Thread.currentThread().getId()));
    	logger.error(message);
    	MDC.remove("threadId");
    }

    public static void error(Logger logger, Throwable ex) {
    	error(logger, ex, null);
    }

    public static void error(Logger logger, Throwable ex, String message) {
    	MDC.put("threadId", Long.toString(Thread.currentThread().getId()));

    	if (message != null) {
    		logger.error(message, ex);
    	} else {    		
    		logger.error(ex.getMessage(), ex);
    	}

    	MDC.remove("threadId");
    }

	private static Logger getLoggerForClass(Class<?> clazz) {
        return loggerMap.computeIfAbsent(clazz, LogManager::getLogger);
    }

	private static void scanAndInitializeLoggers(String basePackage) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(UseLogger.class));

        Set<Class<?>> loggerServiceClasses = scanner.findCandidateComponents(basePackage)
                .stream()
                .map(beanDefinition -> {
                    try {
                        return Class.forName(beanDefinition.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

        loggerServiceClasses.forEach(clazz -> loggerMap.put(clazz, LogManager.getLogger(clazz)));
    }
}

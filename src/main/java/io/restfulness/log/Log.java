package io.restfulness.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.HashMap;
import java.util.Map;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

public class Log {

    private static final Map<Class<?>, Logger> loggerRegistry;

    static {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(Level.INFO);
// naming the logger configuration
        builder.setConfigurationName("DefaultLogger");

// create a console appender
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Console", "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
// add a layout like pattern, json etc
        appenderBuilder.add(builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d %p %c [%t] %m%n"));
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG);
        rootLogger.add(builder.newAppenderRef("Console"));

        builder.add(appenderBuilder);
        builder.add(rootLogger);
        Configurator.reconfigure(builder.build());

        loggerRegistry = new HashMap<>();
    }


    private static final Logger logger = LogManager.getLogger(Log.class);

    public static Logger getLogger(Class<?> cls) {

        if (!loggerRegistry.containsKey(cls))
            loggerRegistry.put(cls, LogManager.getLogger(cls));

        return loggerRegistry.get(cls);
    }

    public static Logger getLogger() {
        StackWalker walker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
        Class<?> cls = walker.getCallerClass();
        return getLogger(cls);
    }
}

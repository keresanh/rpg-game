package org.example.gamefx.utils;

// --enable-logging

import java.util.function.Consumer;
/**
 * Central logging system with configurable output and log levels
 */
public class Logger {
    public enum Level { INFO, WARNING, ERROR }

    private static Logger instance = new Logger();
    private Consumer<String> outputConsumer = System.out::println;
    private boolean enabled = true;

    private Logger() {}

    /**
     * Retrieves singleton logger instance
     *
     * @return The global logger instance
     */
    public static Logger getInstance() {
        return instance;
    }

    /**
     * Enables/disables logging output
     *
     * @param enabled True to enable logging, false to silence
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Logs message with specified severity level
     *
     * @param level Logging severity (INFO/WARNING/ERROR)
     * @param message Text to log
     */
    public void log(Level level, String message) {
        if (!enabled) return;
        String formatted = String.format("[%s] %s", level, message);
        outputConsumer.accept(formatted);
    }

    /**
     * Logs informational message (convenience method)
     *
     * @param message Non-critical status information
     */
    public void info(String message) {
        log(Level.INFO, message);
    }
}

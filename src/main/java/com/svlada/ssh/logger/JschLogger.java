package com.svlada.ssh.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class JschLogger implements com.jcraft.jsch.Logger {

    static final Logger logger = Logger.getLogger(JschLogger.class);

    @Override
    public boolean isEnabled(int level) {

        switch (level) {
            case com.jcraft.jsch.Logger.DEBUG:
                return logger.isEnabledFor(Level.DEBUG);
            case com.jcraft.jsch.Logger.ERROR:
                return logger.isEnabledFor(Level.ERROR);
            case com.jcraft.jsch.Logger.FATAL:
                return logger.isEnabledFor(Level.FATAL);
            case com.jcraft.jsch.Logger.INFO:
                return logger.isEnabledFor(Level.INFO);
            case com.jcraft.jsch.Logger.WARN:
                return logger.isEnabledFor(Level.WARN);
            default:
                return logger.isTraceEnabled();
        }
    }

    @Override
    public void log(int level, String message) {

        switch (level) {
            case com.jcraft.jsch.Logger.DEBUG:
                logger.debug(message);
            case com.jcraft.jsch.Logger.ERROR:
                logger.error(message);
            case com.jcraft.jsch.Logger.FATAL:
                logger.fatal(message);
            case com.jcraft.jsch.Logger.INFO:
                logger.info(message);
            case com.jcraft.jsch.Logger.WARN:
                logger.warn(message);
            default:
                logger.trace(message);
        }
    }
}

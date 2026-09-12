package ru.mephi.lab5.task13;

import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Task13 implements Filter {

    private static final String[] BAD_WORDS = {"sex", "drugs", "c++"};

    @Override
    public boolean isLoggable(LogRecord record) {
        String message = record.getMessage();
        if (message == null) {
            return true;
        }

        String lower = message.toLowerCase();
        for (String bad : BAD_WORDS) {
            if (lower.contains(bad)) {
                return false;
            }
        }
        return true;
    }

    static void main() {
        Logger logger = Logger.getLogger("task8");
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFilter(new Task13());
        logger.addHandler(handler);

        logger.info("Everything is fine");
        logger.info("Let's talk about sex");
        logger.info("drugs are bad");
        logger.info("I prefer C++ to Java");
        logger.info("Java is ok");
    }
}
package ru.mephi.lab5.task14;

import java.io.IOException;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Task14 extends Formatter {

    @Override
    public String getHead(Handler h) {
        return "<!DOCTYPE html>\n<html>\n<head><meta charset=\"utf-8\">"
                + "<title>Log</title></head>\n<body>\n"
                + "<table border=\"1\">\n"
                + "<tr><th>Time</th><th>Level</th><th>Message</th></tr>\n";
    }

    @Override
    public String format(LogRecord record) {
        String time = Instant.ofEpochMilli(record.getMillis()).toString();
        String level = record.getLevel().toString();
        String message = formatMessage(record)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");

        return "<tr><td>" + time + "</td><td>" + level
                + "</td><td>" + message + "</td></tr>\n";
    }

    @Override
    public String getTail(Handler h) {
        return "</table>\n</body>\n</html>\n";
    }

    static void main() throws IOException {
        Logger logger = Logger.getLogger("task9");
        logger.setUseParentHandlers(false);

        FileHandler handler = new FileHandler("log.html", false);
        handler.setFormatter(new Task14());
        logger.addHandler(handler);

        logger.info("Application started");
        logger.warning("Low disk space");
        logger.severe("Something broke <badly> & suddenly");

        handler.close();
        System.out.println("Done, open log.html in a browser");
    }
}
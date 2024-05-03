package pl.brightinventions.slf4android.handler.notifydeveloper;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import pl.brightinventions.slf4android.LogLevel;

class AtLeastFilter implements Filter {
    private final LogLevel level;

    public AtLeastFilter(LogLevel level) {
        this.level = level;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        return level.isSmallerOrEqualTo(record.getLevel())
                ;
    }
}

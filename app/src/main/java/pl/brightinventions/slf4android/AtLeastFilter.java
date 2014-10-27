package pl.brightinventions.slf4android;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

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

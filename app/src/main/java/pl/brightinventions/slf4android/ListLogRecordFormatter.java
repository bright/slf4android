package pl.brightinventions.slf4android;

import java.util.concurrent.ConcurrentLinkedQueue;

class ListLogRecordFormatter implements LogRecordFormatter {
    private ConcurrentLinkedQueue<LoggerPatternValueSupplier> valueSuppliers = new ConcurrentLinkedQueue<LoggerPatternValueSupplier>();

    @Override
    public String format(LogRecord record) {
        StringBuilder logMessage = new StringBuilder();
        for (LoggerPatternValueSupplier supplier : valueSuppliers) {
            supplier.append(record, logMessage);
        }
        return logMessage.toString();
    }

    public void add(LoggerPatternValueSupplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("supplier must not be null");
        }
        valueSuppliers.add(supplier);
    }
}

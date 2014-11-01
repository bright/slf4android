package pl.brightinventions.slf4android;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.logging.FileHandler;

class FileHandlerExpose {
    private static final Logger LOG = LoggerFactory.getLogger(FileHandlerExpose.class.getSimpleName());
    private static Field fileNameField;

    public String getCurrentFileName(FileHandler fileHandler) {
        if (fileHandler == null) {
            throw new IllegalArgumentException("fileHandler must not be null");
        }
        Field field = getFileNameField(fileHandler);
        if (field != null) {
            try {
                return (String) field.get(fileHandler);
            } catch (IllegalAccessException e) {
                LOG.warn("Cant read 'fileName' field value from {}", fileHandler, e);
            }
        }

        return null;
    }

    private synchronized Field getFileNameField(FileHandler fileHandler) {
        //TODO: this can potentially generate multiple errors for the same reason
        if (fileNameField == null) {
            try {
                //TODO: check if there is a better way
                fileNameField = fileHandler.getClass().getDeclaredField("fileName");
                fileNameField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                LOG.warn("Could not find field 'fileName' inside class {}", fileHandler.getClass());
            }
        }
        return fileNameField;
    }
}

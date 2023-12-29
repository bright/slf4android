package pl.brightinventions.slf4android;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.FileHandler;

class FileHandlerExpose {
    private static final Logger LOG = LoggerFactory.getLogger(FileHandlerExpose.class.getSimpleName());
    private static Field fileNameField;
    private static Field filesField;

    public synchronized String getCurrentFileName(FileHandler fileHandler) {
        if (fileHandler == null) {
            throw new IllegalArgumentException("fileHandler must not be null");
        }

        Field filesField = getFilesField(fileHandler);
        if (filesField != null) {
            try {
                File[] files = (File[]) filesField.get(fileHandler);
                return files != null && files.length >= 1 ? files[0].getAbsolutePath() : null;
            } catch (IllegalAccessException e) {
                LOG.warn("Cant read 'files' field value from {}", fileHandler, e);
            }
        }

        Field fileNameField = getFileNameField(fileHandler);
        if (fileNameField != null) {
            try {
                return (String) fileNameField.get(fileHandler);
            } catch (IllegalAccessException e) {
                LOG.warn("Cant read 'fileName' field value from {}", fileHandler, e);
            }
        }

        return null;
    }

    private Field getFileNameField(FileHandler fileHandler) {
        if (fileNameField == null) {
            fileNameField = getFileHandlerField(fileHandler, "fileName");
        }
        return fileNameField;
    }

    private Field getFilesField(FileHandler fileHandler) {
        if (filesField == null) {
            filesField = getFileHandlerField(fileHandler, "files");
        }
        return filesField;
    }

    private Field getFileHandlerField(FileHandler fileHandler, String fieldName) {
        //TODO: this can potentially generate multiple errors for the same reason
        Field field = null;
        try {
            //TODO: check if there is a better way
            field = fileHandler.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LOG.warn("Could not find field '{}' inside class {}", fieldName, fileHandler.getClass());
        }

        return field;
    }
}

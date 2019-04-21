package cz.vondr.photosorter.logger;

import cz.vondr.photosorter.settings.PhotoSorterSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class LoggerInitializer {

    @SuppressWarnings("WeakerAccess")
    public static final String LOG_FILE_PROPERTY_KEY = "photosorter.logFile";

    private PhotoSorterSettings settings;

    public LoggerInitializer(PhotoSorterSettings settings) {
        this.settings = settings;
    }

    public void setupLogger() {
        try {
            configure();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void configure() throws IOException {
        setLogFileLocation();

        Path tmpFile = copyLoggerConfigToTmpFile();
        configureLoggerFromFile(tmpFile);

        LogManager.getLogger(this).info("Logger has been initialized. Log file '" + getLogFile() + "'");
    }

    private void configureLoggerFromFile(Path tmpFile) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(tmpFile.toUri());
    }

    private Path copyLoggerConfigToTmpFile() throws IOException {
        Path tmpFile = Files.createTempFile("PhotoSorter_logger_config_", ".xml");
        tmpFile.toFile().deleteOnExit();
        try (InputStream is = this.getClass().getResourceAsStream("/config/log4j2.xml")) {
            Files.copy(is, tmpFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return tmpFile;
    }

    private void setLogFileLocation() throws IOException {
        System.setProperty(LOG_FILE_PROPERTY_KEY, getLogFile().getCanonicalPath());
    }

    private File getLogFile() {
        File logFile;
        if (settings.getLogFile() == null) {
            logFile = new File(settings.getDestination(), ".photosorter/logs/PhotoSorter.log");
        } else {
            logFile = settings.getLogFile();
        }
        return logFile;
    }


}

package cz.vondr.photosorter.logger

import cz.vondr.photosorter.PhotoSorter
import cz.vondr.photosorter.settings.PhotoSorterSettings
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory
import org.slf4j.LoggerFactory

class LoggerInitializer {

    public static final String LOG_FILE_PROPERTY_KEY = "photosorter.logFile"

    private PhotoSorterSettings settings

    LoggerInitializer(PhotoSorterSettings settings) {
        this.settings = settings
    }

    void setupLogger() {

        setLogFileLocation()
        //        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        //        File file = new File("path/to/a/different/log4j2.xml");
        //
        //        // this will force a reconfiguration
        //        context.setConfigLocation(file.toURI());
        //
        //
        //        ConfigurationSource source = new ConfigurationSource(Main.class.getResourceAsStream("/in/gunbound/prelauncher/server/log4j2/log4j2.xml"));
        //        Configuration factory = ConfigurationFactory.getInstance().getConfiguration(source);
        //        ConfigurationFactory.setConfigurationFactory(factory);
        //
        //        ConfigurationFactory.getInstance()
        //        XMLConfigurationFactory.getInstance().getConfiguration(source)


        InputStream is = this.class.getResourceAsStream("/config/log4j2.xml")
        ConfigurationSource source = new ConfigurationSource(is)
        LoggerContext ctx = (LoggerContext) LogManager.getContext(true)
        Configuration config = XmlConfigurationFactory.getInstance().getConfiguration(ctx, source)

        ctx.stop();
        ctx.start(config);

        //        Logger localLogger = LoggerFactory.getLogger(PhotoSorter.class)
        Logger localLogger = ctx.getLogger(PhotoSorter.class.name)
        localLogger.error("CONFIGUREDDDDDDDDDDDDDDDDDDDDDDDD")
        localLogger.info("CONFIGUREDDDDDDDDDDDDDDDDDDDDDDDD info")

        LoggerContext context = (LoggerContext) LogManager.getContext(false);

        context.setExternalContext(ctx)

        ctx.updateLoggers(config)
        ctx.reconfigure()


        Configurator.initialize(config)

        String message = "Logger have been initialized"
        org.slf4j.Logger hmmmm = LoggerFactory.getLogger(PhotoSorter.class)
        hmmmm.info("AAAAAAAAAAAAA===========================")
        hmmmm.error("AAAAAAAAAAAAA===========================EEEEE")
    }

    private String setLogFileLocation() {
        System.setProperty(LOG_FILE_PROPERTY_KEY, logFile.canonicalPath)
    }

    private File getLogFile() {
        File logFile
        if (settings.logFile == null) {
            logFile = new File(settings.destination, ".photosorter/logs/PhotoSorter.log")
        } else {
            logFile = settings.logFile
        }
        logFile
    }

    //                databaseDirectory: new File("d:/foto/.photosorter/database/")
    //TODO configure log directory default $destination/.photosorter/logs
}

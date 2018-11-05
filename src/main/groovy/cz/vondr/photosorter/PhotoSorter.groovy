package cz.vondr.photosorter

import cz.vondr.photosorter.date_resolver.AllTypesDateResolver
import cz.vondr.photosorter.date_resolver.DateResolver
import cz.vondr.photosorter.date_resolver.DateResult
import cz.vondr.photosorter.finder.SourceFileFinder
import cz.vondr.photosorter.indexer.api.Database
import cz.vondr.photosorter.indexer.api.PhotoInfo
import cz.vondr.photosorter.indexer.dummy.DummyDatabase
import cz.vondr.photosorter.indexer.elastic.ElasticDatabase
import cz.vondr.photosorter.logger.LoggerInitializer
import cz.vondr.photosorter.settings.PhotoSorterSettings
import cz.vondr.photosorter.util.FileNameFormatter
import cz.vondr.photosorter.util.PhotoCrc
import cz.vondr.photosorter.util.Utils
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static cz.vondr.photosorter.settings.FileOperation.COPY
import static cz.vondr.photosorter.settings.FileOperation.INDEX
import static cz.vondr.photosorter.settings.FileOperation.MOVE
import static cz.vondr.photosorter.settings.FileOperation.NOTHING

class PhotoSorter {

    private static final Logger logger = LoggerFactory.getLogger(PhotoSorter.class)

    private PhotoSorterSettings settings

    private FileNameFormatter fileNameFormatter = new FileNameFormatter()

    private Database database

    private DateResolver dateResolver = new AllTypesDateResolver()

    PhotoSorter(PhotoSorterSettings settings) {
        this.settings = settings
        this.settings.validate()
        setupDatabase()
    }

    private void setupDatabase() {
        if (settings.useDatabase) {
            File databaseDirectory
            if (settings.databaseDirectory == null) {
                databaseDirectory = new File(settings.destination, ".photosorter/database")
            } else {
                databaseDirectory = settings.databaseDirectory
            }
            database = new ElasticDatabase(databaseDirectory: databaseDirectory.absolutePath)
        } else {
            logger.warn "Warning - no databaseDirectory is configured - same files can be copied multiple times"
            database = new DummyDatabase()
        }
    }

    void sort() {
        new LoggerInitializer().setupLogger()

        logger.info "Sorting is starting with settings $settings"
        database.start()
        try {
            new SourceFileFinder(settings).forEachFile {
                processFile(it)
            }
        } catch (Exception ex) {
            logger.error("", ex)
            throw ex
        } finally {
            database.stop()
        }

        logger.info "Sorting have just finished"
    }



    private void processFile(File image) {
        logger.info "Processing file '$image'"

        PhotoCrc photoCrc = new PhotoCrc(image)
        if (database.contains(photoCrc.crc)) {
            logger.warn "Warning - file '$image' was already processed before. Nothing will be done now."
            return
        }

        DateResult dateResult = dateResolver.resolveDate(image)
        if (!dateResult.resolvedSuccessfully) {
            logger.error "ERROR - Cannot obtain date from image '$image'. Image will be skipped."
            return
        }
        Date date = dateResult.date

        File destinationFile = getDestinationFile(date, image)

        moveOrCopyFile(image, destinationFile, date)

        if (settings.fileOperation != NOTHING) {
            PhotoInfo photoInfo = new PhotoInfo(
                    crc: photoCrc,
                    originalPath: image.absolutePath,
                    newPath: destinationFile.absolutePath,
                    originalName: image.name,
                    newName: destinationFile.name,
                    indexedDate: new Date(),
                    takenDate: date,
            )
            database.insert(photoInfo)
        }
    }

    private void moveOrCopyFile(File image, File destinationFile, Date date) {
        switch (settings.fileOperation) {
            case MOVE:
                logger.info "moving file '$image' into '$destinationFile' "
                FileUtils.moveFile(image, destinationFile)
                destinationFile.setLastModified(date.getTime())
                break
            case COPY:
                logger.info "copying file '$image' into '$destinationFile' "
                FileUtils.copyFile(image, destinationFile)
                destinationFile.setLastModified(date.getTime())
                break
            case INDEX:
                logger.info "indexing file '$image' "
                break
            case NOTHING:
                logger.info "file will be processed '$image'"
                break
            default:
                throw new IllegalStateException("Unsupported option $settings.fileOperation")
        }
    }

    private File getDestinationFile(Date date, File image) {
        def folderName = Utils.getFolderName(date)
        def destinationFolder = new File(settings.destination, folderName)

        File destinationFile = null
        for (int i = 1; destinationFile == null; i++) {
            if (i >= 10000) {
                throw new IllegalStateException("Cannot find free filename for file '$image.name' in directory $destinationFolder.canonicalPath'")
            }
            def filename = fileNameFormatter.format(date, image.name, i)
            destinationFile = new File(destinationFolder, filename)
            if (destinationFile.exists()) {
                logger.trace("File ${destinationFile.canonicalPath} already exists. Trying anotherone")
                destinationFile = null
            }
        }

        destinationFile
    }

}




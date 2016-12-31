package cz.vondr.photosorter

import cz.vondr.photosorter.indexer.api.Database
import cz.vondr.photosorter.indexer.api.PhotoInfo
import cz.vondr.photosorter.indexer.dummy.DummyDatabase
import cz.vondr.photosorter.indexer.elastic.ElasticDatabase
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

    private Database database = new ElasticDatabase()

    PhotoSorter(PhotoSorterSettings settings) {
        this.settings = settings
        this.settings.validate()
        setupDatabase()
    }

    private void setupDatabase() {
        if (settings.databaseDirectory == null) {
            logger.warn "Warning - no databaseDirectory is configured - same files can be copied multiple times"
            database = new DummyDatabase()
        } else {
            database = new ElasticDatabase(databaseDirectory: settings.databaseDirectory.absolutePath)
        }
    }

    public void sort() {
        logger.info "Sorting is starting with settings $settings"
        database.start()
        try {
            settings.source.eachFileMatch(~/(?i).*\.jpg/) {
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

        Date date = Utils.getImageDate(image)
        if (date == null) {
            logger.error "ERROR - Cannot obtain date from image '$image'. Image will be skipped."
            return
        }

        File destinationFile = getDestinationFile(date, image)

        moveOrCopyFile(image, destinationFile)

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

        destinationFile.setLastModified(date.getTime())
    }

    private void moveOrCopyFile(File image, File destinationFile) {
        //TODO mozna osefovat, kdyby se tam nejaky soubor uz tak jmenoval
        switch (settings.fileOperation) {
            case MOVE:
                logger.info "moving file '$image' into '$destinationFile' "
                FileUtils.moveFile(image, destinationFile)
                break
            case COPY:
                logger.info "copying file '$image' into '$destinationFile' "
                FileUtils.copyFile(image, destinationFile)
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
        def filename = fileNameFormatter.format(date, image.name)
        def destinationFile = new File(destinationFolder, filename)
        destinationFile
    }

}




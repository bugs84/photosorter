package cz.fotosorter

import cz.fotosorter.indexer.api.Database
import cz.fotosorter.indexer.api.PhotoInfo
import cz.fotosorter.indexer.dummy.DummyDatabase
import cz.fotosorter.indexer.elastic.ElasticDatabase
import cz.fotosorter.util.PhotoCrc
import cz.fotosorter.util.Utils
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static cz.fotosorter.MoveOrCopy.COPY
import static cz.fotosorter.MoveOrCopy.MOVE

class PhotoSorter {

    private static final Logger logger = LoggerFactory.getLogger(PhotoSorter.class)

    private PhotoSorterSettings settings

    private FileNameFormatter fileNameFormatter = new FileNameFormatter()

    private Database database = new ElasticDatabase()

    PhotoSorter(PhotoSorterSettings settings) {
        this.settings = settings
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

        settings.source.eachFileMatch(~/(?i).*\.jpg/) {
            processFile(it)
        }

        database.stop()
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

        PhotoInfo photoInfo = new PhotoInfo(
                crc: photoCrc,
                originalPath: image.absolutePath,
                newPath: destinationFile.absolutePath,
                originalName: image.name,
                newName: destinationFile.name,
        //TODO dalsi udaje do photo infa
        )
        database.insert(photoInfo)

        destinationFile.setLastModified(date.getTime())
    }

    private void moveOrCopyFile(File image, File destinationFile) {
        //TODO mozna osefovat, kdyby se tam nejaky soubor uz tak jmenoval
        switch (settings.moveOrCopy) {
            case MOVE:
                logger.info "moving file '$image' into '$destinationFile' "
                FileUtils.moveFile(image, destinationFile)
                break
            case COPY:
                logger.info "copying file '$image' into '$destinationFile' "
                FileUtils.copyFile(image, destinationFile)
                break
            default:
                throw new IllegalStateException("Unsupported option $settings.moveOrCopy")
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




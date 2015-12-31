package cz.photosorter

import cz.photosorter.indexer.api.Database
import cz.photosorter.indexer.api.PhotoInfo
import cz.photosorter.indexer.elastic.ElasticDatabase
import cz.photosorter.util.PhotoCrc
import cz.photosorter.util.Utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static groovy.io.FileType.FILES

/**
 * I use it to add images into database without sorting (moving) it
 */
class PhotoIndexer {
    private static final Logger logger = LoggerFactory.getLogger(PhotoIndexer.class)

    private Database database
    File databaseDirectory
    File directoryToIndex


    private void setupDatabase() {
        if (databaseDirectory == null) {
            throw new NullPointerException("databaseDirectory")
        } else {
            database = new ElasticDatabase(databaseDirectory: databaseDirectory.absolutePath)
        }
    }

    public void indexFiles() {
        setupDatabase()

        logger.info "Indexing is starting, database directory: '$databaseDirectory'"
        database.start()
        try {
            directoryToIndex.eachFileRecurse(FILES) { file ->
                if (file.name.matches(~/(?i).*\.jpg/)) {
                    processFile(file)
                }
            }
        } catch (Exception ex) {
            logger.error("", ex)
            throw ex
        } finally {
            database.stop()
        }

        logger.info "Indexing have just finished"
    }

    private void processFile(File image) {
        logger.info "Processing file '$image'"

        PhotoCrc photoCrc = new PhotoCrc(image)
        if (database.contains(photoCrc.crc)) {
            logger.info "Warning - file '$image' was already indexed before. Nothing will be done now."
            return
        }

        Date date = Utils.getImageDate(image)
        if (date == null) {
            logger.error "ERROR - Cannot obtain date from image '$image'. Image will be skipped."
            return
        }

        PhotoInfo photoInfo = new PhotoInfo(
                crc: photoCrc,
                originalPath: image.absolutePath,
                newPath: image.absolutePath,
                originalName: image.name,
                newName: image.absolutePath,
                indexedDate: new Date(),
                takenDate: date,
                additionalInfo: "added by indexer"
        )
        logger.info "Going to index '$image'"
        database.insert(photoInfo)

    }

    // START -----------

    public static void main(String[] args) {
        PhotoIndexer app = new PhotoIndexer(
                databaseDirectory: new File("c:\\tmp\\zkouseni_photoSorteru\\database"),
                directoryToIndex: new File("c:\\tmp\\zkouseni_photoSorteru\\"))
        app.indexFiles()
    }
}

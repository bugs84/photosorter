package cz.vondr.photosorter

import cz.vondr.photosorter.indexer.api.Database
import cz.vondr.photosorter.indexer.elastic.ElasticDatabase


/**
 * Starts elastic database - read input - and then stop it.
 * I used it for quering database by rest api or trying Kibana.
 */
class PhotoDatabaseStarter {
    private Database database
    File databaseDirectory

    private void setupDatabase() {
        if (databaseDirectory == null) {
            throw new NullPointerException("databaseDirectory")
        } else {
            database = new ElasticDatabase(databaseDirectory: databaseDirectory.absolutePath)
        }
    }

    private void start() {
        setupDatabase()
        database.start()
        try {
            println "Press enter to stop database"
            System.in.read()
        } finally {
            database.stop()
        }
    }

    public static void main(String[] args) {
        new PhotoDatabaseStarter(
                databaseDirectory: new File("c:\\tmp\\zkouseni_photoSorteru\\database")
        ).start()
    }
}

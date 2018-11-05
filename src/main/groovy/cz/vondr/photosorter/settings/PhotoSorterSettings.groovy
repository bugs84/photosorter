package cz.vondr.photosorter.settings

import groovy.transform.ToString

import static FileOperation.COPY

@ToString
class PhotoSorterSettings {
    File source
    File destination

    /** If files should be copied, moved, or indexed.<br>
     * Index mean, that file is only added into database and nothing else is done with file.<br>
     *  Default is COPY */
    FileOperation fileOperation = COPY

    /** If database should be used.<br>
     * Into database are stored information about processed files.<br>
     * Thanks to it same file will not be copied multiple times.<br>
     *
     * Default value is true */
    Boolean useDatabase = true
    /** Directory where will be stored information about files, which was already processed.<br>
     * Thanks to it same file will not be copied multiple times.<br>
     * If null - database directory will be in "${destination}/.photosorter/database/"<br>
     * Default value is null */
    File databaseDirectory = null

    /** Log File location.<br>
     * If null - log file will be in "${destination}/.photosorter/logs/PhotoSorter.log"<br>
     * Default value is null */
    File logFile = null

    def validate() {
        assert source != null
        assert destination != null
    }
}

enum FileOperation {
    MOVE,
    COPY,
    /** File will be added into database as indexed and nothing else is done with file */
    INDEX,
    /** Nothing is done. This will only write what will be done with photo, but nothing else. */
    NOTHING,
}

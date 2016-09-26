package cz.vondr.photosorter.settings

import groovy.transform.ToString

import static FileOperation.COPY

@ToString
class PhotoSorterSettings {
    File source
    File destination

    /** If files should be copied, moved, or indexed.
     * Index mean, that file is only added into database and nothing else is done with file.
     *  Default is COPY */
    FileOperation fileOperation = COPY

    /** Directory where will be stored information about files, which was already processed.
     * Thanks to it same file will not be copied multiple times.
     * If null - no database will be used.
     * Default value is null*/
    File databaseDirectory = null

    def validate() {
        assert source != null
        assert destination != null
    }
}

enum FileOperation {
    MOVE,
    COPY,
    INDEX,
}

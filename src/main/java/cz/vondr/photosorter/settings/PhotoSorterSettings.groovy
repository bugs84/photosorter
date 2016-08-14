package cz.vondr.photosorter.settings

import groovy.transform.ToString

import static cz.vondr.photosorter.settings.MoveOrCopy.COPY

@ToString
class PhotoSorterSettings {
    File source
    File destination

    /** If files should be copied, or moved. Default is COPY */
    MoveOrCopy moveOrCopy = COPY

    /** directory where will be stored information about files, which was already processed.
     * Thanks to it same file will not be copied multiple times */
    File databaseDirectory

    def validate() {
        assert source != null
        assert destination != null
    }
}

enum MoveOrCopy {
    MOVE,
    COPY,
}

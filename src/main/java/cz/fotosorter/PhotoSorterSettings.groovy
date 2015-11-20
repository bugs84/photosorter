package cz.fotosorter

import groovy.transform.ToString

import static cz.fotosorter.MoveOrCopy.COPY

@ToString
class PhotoSorterSettings {
    File source
    File destination

    /** If files should be copied, or moved. Deafult is COPY */
    MoveOrCopy moveOrCopy = COPY

    /** directory where will be stored information about files, which was already processed.
     * Thanks to it same file will not be copied multiple times */
    File databaseDirectory
}

enum MoveOrCopy {
    MOVE,
    COPY,
}

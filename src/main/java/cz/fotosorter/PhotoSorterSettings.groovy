package cz.fotosorter

import static cz.fotosorter.MoveOrCopy.COPY

class PhotoSorterSettings {
    File source
    File destination
    MoveOrCopy moveOrCopy = COPY
}

enum MoveOrCopy {
    MOVE,
    COPY,
}

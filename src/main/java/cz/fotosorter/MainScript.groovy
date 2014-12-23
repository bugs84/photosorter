package cz.fotosorter

import static cz.fotosorter.MoveOrCopy.MOVE


def photoSorter = new PhotoSorter(new PhotoSorterSettings(
        source: new File("c:\\foto\\AANikkonTransfer\\toSort"),
        destination: new File("c:\\foto\\2014"),
        moveOrCopy: MOVE,
))
photoSorter.sort()


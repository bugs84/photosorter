package cz.photosorter

import static cz.photosorter.MoveOrCopy.MOVE


def photoSorter = new PhotoSorter(new PhotoSorterSettings(
//        source: new File("c:\\foto\\AANikkonTransfer\\toSort\\"),
        source: new File("c:\\foto\\AA_Mi2S_Transfer\\Camera\\"),
//        source: new File("c:\\foto\\AA_Canon_G12_Transfer\\"),
        destination: new File("c:\\foto"),
        moveOrCopy: MOVE,
))
photoSorter.sort()


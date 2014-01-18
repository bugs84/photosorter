package cz.fotosorter

File source = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorter_SOURCE")
File destination = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorterOUTPUT")

def photoSorter = new PhotoSorter()
photoSorter.moveFiles(source, destination)


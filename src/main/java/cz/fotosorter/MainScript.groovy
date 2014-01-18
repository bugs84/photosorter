package cz.fotosorter

File source = new File("c:\\foto\\AANikkonTransfer\\004")
File destination = new File("c:\\foto")

def photoSorter = new PhotoSorter()
photoSorter.moveFiles(source, destination)


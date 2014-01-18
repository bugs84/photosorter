package cz.fotosorter

def source = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorter_SOURCE")
def destination = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorterOUTPUT")

source.eachFile {
    processFile()
}

private void processFile() {
    println "read and move"
}


package cz.fotosorter

import groovy.transform.Field
import org.apache.commons.io.FileUtils

def source = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorter_SOURCE")
@Field
def destination = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorterOUTPUT")

source.eachFile {
    processFile(it)

}

private void processFile(File image) {
    Date date = Utils.getImageDate(image)

    File destinationFile = getDestinationFile(date, image)

    println "moving file '$image' into '$destinationFile' "
    FileUtils.moveFile(image, destinationFile)
//    FileUtils.copyFile(image, destinationFile)

    destinationFile.setLastModified(date.getTime())
}

private File getDestinationFile(Date date, File image) {
    def folderName = Utils.getFolderName(date)
    def destinationFolder = new File(destination, folderName)
    def destinationFile = new File(destinationFolder, image.name)
    destinationFile
}




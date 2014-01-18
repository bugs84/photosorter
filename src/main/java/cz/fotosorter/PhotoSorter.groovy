package cz.fotosorter

import org.apache.commons.io.FileUtils

class PhotoSorter {
    File source
    File destination

    public void moveFiles(File sourceDirectory, File destinationDirectory) {
        this.source = sourceDirectory
        this.destination = destinationDirectory
        this.source.eachFile {
            processFile(it)
        }
    }

    private void processFile(File image) {
        Date date = Utils.getImageDate(image)

        if (date == null) {
            println "ERROR - cannot obtain date from image '" + image + "'"
        }

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

}




package cz.fotosorter

import org.apache.commons.io.FileUtils

import static cz.fotosorter.MoveOrCopy.COPY
import static cz.fotosorter.MoveOrCopy.MOVE


class PhotoSorter {

    PhotoSorterSettings settings

    PhotoSorter(PhotoSorterSettings settings) {
        this.settings = settings
    }

    public void sort() {
        settings.source.eachFileMatch(~/(?i).*\.jpg/) {
            processFile(it)
        }
    }

    private void processFile(File image) {
        Date date = Utils.getImageDate(image)

        if (date == null) {
            println "ERROR - cannot obtain date from image '" + image + "'"
        }

        File destinationFile = getDestinationFile(date, image)

        moveOrCopyFile(image, destinationFile)



        destinationFile.setLastModified(date.getTime())
    }

    private void moveOrCopyFile(File image, File destinationFile) {
        switch (settings.moveOrCopy) {
            case MOVE:
                println "moving file '$image' into '$destinationFile' "
                FileUtils.moveFile(image, destinationFile)
                break
            case COPY:
                println "copying file '$image' into '$destinationFile' "
                FileUtils.copyFile(image, destinationFile)
                break
            default:
                throw IllegalStateException("Unsopported option " + settings.moveOrCopy)
        }
    }

    private File getDestinationFile(Date date, File image) {
        def folderName = Utils.getFolderName(date)
        def destinationFolder = new File(settings.destination, folderName)
        def destinationFile = new File(destinationFolder, image.name)
        destinationFile
    }

}




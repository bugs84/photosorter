package cz.fotosorter

import cz.fotosorter.util.Utils
import org.apache.commons.io.FileUtils

import static cz.fotosorter.MoveOrCopy.COPY
import static cz.fotosorter.MoveOrCopy.MOVE


class PhotoSorter {

    PhotoSorterSettings settings

    private FileNameFormatter fileNameFormatter = new FileNameFormatter()

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
            println "ERROR - Cannot obtain date from image '$image'. Image will be skipped."
            return
        }

        File destinationFile = getDestinationFile(date, image)

        moveOrCopyFile(image, destinationFile)



        destinationFile.setLastModified(date.getTime())
    }

    private void moveOrCopyFile(File image, File destinationFile) {
        //TODO mozna osefovat, kdyby se tam nejaky soubor uz tak jmenoval
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
                throw new IllegalStateException("Unsupported option " + settings.moveOrCopy)
        }
    }

    private File getDestinationFile(Date date, File image) {
        def folderName = Utils.getFolderName(date)
        def destinationFolder = new File(settings.destination, folderName)
        def filename = fileNameFormatter.format(date, image.name)
        def destinationFile = new File(destinationFolder, filename)
        destinationFile
    }

}




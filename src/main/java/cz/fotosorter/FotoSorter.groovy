package cz.fotosorter

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifSubIFDDirectory

def source = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorter_SOURCE")
def destination = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorterOUTPUT")

source.eachFile {
    processFile(it)

}

private void processFile(File image) {
    def metadata = ImageMetadataReader.readMetadata(image);
    ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
    Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

    println "$date - $image.name"



}


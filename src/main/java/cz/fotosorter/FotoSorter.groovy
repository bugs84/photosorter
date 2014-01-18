package cz.fotosorter

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifSubIFDDirectory

def source = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorter_SOURCE")
def destination = new File("c:\\foto\\AANikkonTransfer\\004_DELETE_FotoSorterOUTPUT")

source.eachFile {
    processFile(it)

}

private void processFile(File image) {
    Date date = getImageDate(image)

    String folder = getFolder(date)

    println "$folder - $date - $image.name"



}

private Date getImageDate(File image) {
    def metadata = ImageMetadataReader.readMetadata(image);
    ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
    Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    date
}

private String getFolder(Date date) {
    def c = Calendar.getInstance()
    c.setTime(date)
    def year = c.get(Calendar.YEAR)
    def month = c.get(Calendar.MONTH) + 1
    def day = c.get(Calendar.DAY_OF_MONTH)
    String.format("%d_%02d_%02d", year, month, day)
}


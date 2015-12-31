package cz.vondr.photosorter.util
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.exif.ExifSubIFDDirectory

class Utils {
    static String getFolderName(Date date) {
        def c = Calendar.getInstance()
        c.setTime(date)
        def year = c.get(Calendar.YEAR)
        def month = c.get(Calendar.MONTH) + 1
        def day = c.get(Calendar.DAY_OF_MONTH)
        String.format("%d_%02d_%02d", year, month, day)
    }

    static Date getImageDate(File image) {
        //tohle ukaze vsechny metadata:
        //ImageMetadataReader.readMetadata(image)
        def metadata = ImageMetadataReader.readMetadata(image);

        ExifSubIFDDirectory exifSubIFDDirectory = metadata.getDirectory(ExifSubIFDDirectory.class);
        Date date = exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

        if (date == null) {
            ExifIFD0Directory exifIFD0Directory = metadata.getDirectory(ExifIFD0Directory.class);
            date = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
        }

        date
    }
}

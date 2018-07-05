package cz.vondr.photosorter.date_resolver

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.exif.ExifSubIFDDirectory

class JpgImageMetadataResolver implements DateResolver {

    @Override
    DateResult resolveDate(File file) {
        if (!file.name.toLowerCase(Locale.US).endsWith(".jpg")) {
            return new DateResult(resolvedSuccessfully: false)
        }

        Date date = getImageDate(file)
        new DateResult(
                resolvedSuccessfully: date != null,
                date: date
        )
    }

    private Date getImageDate(File image) {
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

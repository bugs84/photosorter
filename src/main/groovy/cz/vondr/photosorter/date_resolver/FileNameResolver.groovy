package cz.vondr.photosorter.date_resolver

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.time.FastDateFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.text.ParseException

import static cz.vondr.photosorter.date_resolver.DateResult.NOT_RESOLVED

abstract class FileNameResolver implements DateResolver {

    private static final Logger logger = LoggerFactory.getLogger(FileNameResolver.class)

    abstract String getDateFormat()
    abstract String getFileExtension()

    private FastDateFormat fastDateFormat = FastDateFormat.getInstance(dateFormat)

    @Override
    DateResult resolveDate(File file) {
        if (!file.name.toLowerCase(Locale.US).endsWith(fileExtension.toLowerCase())) {
            return NOT_RESOLVED
        }

        String fileNameWithoutExtension = FilenameUtils.removeExtension(file.name)

        try {
            Date date = fastDateFormat.parse(fileNameWithoutExtension)
            return new DateResult(
                    resolvedSuccessfully: true,
                    date: date)
        } catch (ParseException ex) {
            logger.trace("Cannot parse date from file '$file'.", ex);
            return NOT_RESOLVED
        }

    }
}

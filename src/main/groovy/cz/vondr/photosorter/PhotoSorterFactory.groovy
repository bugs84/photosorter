package cz.vondr.photosorter

import cz.vondr.photosorter.logger.LoggerInitializer
import cz.vondr.photosorter.settings.PhotoSorterSettings

class PhotoSorterFactory {

    static createPhotoSorter(PhotoSorterSettings settings) {
        new LoggerInitializer(settings).setupLogger()

        return new PhotoSorter(settings)

    }
}

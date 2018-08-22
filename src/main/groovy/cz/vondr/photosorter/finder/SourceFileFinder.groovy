package cz.vondr.photosorter.finder

import cz.vondr.photosorter.settings.PhotoSorterSettings
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

class SourceFileFinder {
    final PhotoSorterSettings settings

    SourceFileFinder(PhotoSorterSettings settings) {
        this.settings = settings
    }

    void forEachFile(@ClosureParams(value = SimpleType.class, options = "java.io.File") Closure closure) {
        settings.source.eachFileRecurse { file ->
            def lowerCaseName = file.name.toLowerCase()
            if (lowerCaseName.endsWith(".jpg") /*|| lowerCaseName.endsWith(".mp4")*/) {
                closure(file)
            }
        }
    }
}

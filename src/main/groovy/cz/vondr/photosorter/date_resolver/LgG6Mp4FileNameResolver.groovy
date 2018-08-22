package cz.vondr.photosorter.date_resolver

class LgG6Mp4FileNameResolver extends FileNameResolver {

    @Override
    String getDateFormat() {
        "yyyyMMdd_HHmmss"
    }

    @Override
    String getFileExtension() {
        ".mp4"
    }
}

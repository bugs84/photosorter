package cz.vondr.photosorter.date_resolver

class Mi2sMp4FileNameResolver extends FileNameResolver {

    @Override
    String getDateFormat() {
        "'VID_'yyyyMMdd_HHmmss"
    }

    @Override
    String getFileExtension() {
        ".mp4"
    }
}

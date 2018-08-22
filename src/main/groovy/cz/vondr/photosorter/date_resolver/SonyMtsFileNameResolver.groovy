package cz.vondr.photosorter.date_resolver

class SonyMtsFileNameResolver extends FileNameResolver {

    @Override
    String getDateFormat() {
        "yyyyMMddHHmmss"
    }

    @Override
    String getFileExtension() {
        ".mts"
    }
}

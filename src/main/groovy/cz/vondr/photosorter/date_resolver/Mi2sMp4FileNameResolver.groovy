package cz.vondr.photosorter.date_resolver

class Mi2sMp4FileNameResolver extends Mp4FileNameResolver {

    @Override
    String getDateFormat() {
        "'VID_'yyyyMMdd_HHmmss"
    }
}

package cz.fotosorter.indexer.api

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes = ["crc"])
@ToString
class PhotoInfo {
    String crc
    String originalPath
    String originalName
    String newPath
    String newName
    //todo indexed date
    //todo date when was photo taken
}

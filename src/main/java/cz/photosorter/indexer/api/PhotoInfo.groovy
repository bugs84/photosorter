package cz.photosorter.indexer.api

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
    /** date when was photo taken */
    Date takenDate
    /** date when photo was indexed */
    Date indexedDate
    String additionalInfo
}

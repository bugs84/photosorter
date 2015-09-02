package cz.fotosorter.util

import org.apache.commons.io.FileUtils

class PhotoHash {
    private File file
    private long checksum

    PhotoHash(File file) {
        this.file = file
        doChecksum()
    }

    private long doChecksum() {
        checksum = FileUtils.checksumCRC32(file)
    }

    long getChecksum() {
        return checksum
    }
}

package cz.photosorter.util

import org.apache.commons.io.FileUtils

class PhotoCrc {
    private File file
    private String crc

    PhotoCrc(File file) {
        this.file = file
        doChecksum()
    }

    private void doChecksum() {
        crc = FileUtils.checksumCRC32(file).toString()
    }

    String getCrc() {
        crc
    }

}

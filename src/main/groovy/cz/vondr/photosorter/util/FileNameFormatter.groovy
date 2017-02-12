package cz.vondr.photosorter.util

import org.apache.commons.io.FilenameUtils

import java.text.SimpleDateFormat

class FileNameFormatter {

    def sdf = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")

    public String format(Date date, String originalFileName, int fileNumber = 1) {
        def nameWithoutExtension = FilenameUtils.getBaseName(originalFileName)
        def extensionWithDot = FilenameUtils.getExtension(originalFileName)
        extensionWithDot = extensionWithDot.isEmpty() ? extensionWithDot : "." + extensionWithDot
        return "${sdf.format(date)}__${nameWithoutExtension}${fileNumber == 1 ? "" : "_$fileNumber"}$extensionWithDot"
    }
}

package cz.vondr.photosorter.util

import java.text.SimpleDateFormat

class FileNameFormatter {

    def sdf = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")

    public String format(Date date, String originalFileName) {
        "${sdf.format(date)}__$originalFileName"
    }
}

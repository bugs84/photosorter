package cz.vondr.photosorter

import cz.vondr.photosorter.util.FileNameFormatter
import org.junit.Test

class FileNameFormatterTest {

    def fileNameFormatter = new FileNameFormatter()

    @Test
    void 'file name format test'() {
        //1.2.2015 18:03:04
        def date = new GregorianCalendar(2015, 0, 2, 18, 3, 4).time

        def name = fileNameFormatter.format(date, "IMG original name.jpg")

        assert name == "2015_01_02_18-03-04__IMG original name.jpg"
    }
}

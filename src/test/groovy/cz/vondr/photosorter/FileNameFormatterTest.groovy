package cz.vondr.photosorter

import cz.vondr.photosorter.util.FileNameFormatter
import org.junit.Test

class FileNameFormatterTest {

    def fileNameFormatter = new FileNameFormatter()

    @Test
    void 'file name format test'() {
        def date = new GregorianCalendar(2015, 0, 2, 18, 3, 4).time //1.2.2015 18:03:04

        def name = fileNameFormatter.format(date, "IMG original name.jpg")

        assert name == "2015_01_02_18-03-04__IMG original name.jpg"
    }

    @Test
    void 'format put number to name'() {
        def date = new GregorianCalendar(2015, 0, 2, 18, 3, 4).time //1.2.2015 18:03:04

        def name = fileNameFormatter.format(date, "IMG original name.jpg", 789)

        assert name == "2015_01_02_18-03-04__IMG original name_789.jpg"
    }

    @Test
    void 'format do not put number in name when number is 1'() {
        def date = new GregorianCalendar(2015, 0, 2, 18, 3, 4).time //1.2.2015 18:03:04

        def name = fileNameFormatter.format(date, "IMG original name.jpg", 1)

        assert name == "2015_01_02_18-03-04__IMG original name.jpg"
    }

    @Test
        void 'name without dot works '() {
            def date = new GregorianCalendar(2015, 0, 2, 18, 3, 4).time //1.2.2015 18:03:04

            def name = fileNameFormatter.format(date, "name without dot", 789)

            assert name == "2015_01_02_18-03-04__name without dot_789"
        }
}

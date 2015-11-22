package cz.photosorter

import cz.photosorter.util.Utils
import org.junit.Test


class UtilsTest {

    @Test
    void getFolderNameReturnCorrectFormat() {
        def c = Calendar.getInstance()
        c.set(2010, 0, 1)
        def date = c.getTime()

        assert "2010_01_01" == Utils.getFolderName(date)
    }

    @Test(expected = NullPointerException.class)
    void getFolderNameThrowExceptionWhenDateIsNull() {
        Utils.getFolderName(null)
    }

}

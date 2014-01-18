package cz.fotosorter

import org.junit.Test


class UtilsTest {

    @Test
    void getFolderNameReturnCorrectFormat() {
        def c = Calendar.getInstance()
        c.set(2010, 0, 1)
        def date = c.getTime()

        assert "2010_01_01" == Utils.getFolderName(date)
    }

//    @Test
//    void getFileNameReturnCorrectFileName() {
//        def c = Calendar.getInstance()
//        c.set(2010, 0, 1,2,4)
//        def date = c.getTime()
//
//        def file = new File("/tmp/fakteTestFile/DSC_115.jpg");
//        assert "2010_01_01-02-04_DSC_115.jpg" == Utils.getFileName(file, date)
//    }


    @Test
    void getFileNameReturnCorrectFileName() {
        def c = Calendar.getInstance()
        c.set(2010, 0, 1, 2, 4)
        def date = c.getTime()


        def file = new File("/tmp/fakteTestFile/DSC_115.jpg");

        assert "2010_01_01-02-04_DSC_115.jpg" == Utils.getFileName(file, date)
    }
}

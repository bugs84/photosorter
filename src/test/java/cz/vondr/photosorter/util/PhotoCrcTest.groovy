package cz.vondr.photosorter.util

import org.apache.commons.io.FileUtils
import org.codehaus.groovy.runtime.IOGroovyMethods
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class PhotoCrcTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    void 'compute hash of lena file'() {
        File lenaFile = copyLenaFromResourceToFile()

        assert new PhotoCrc(lenaFile).crc == "3270423915"
    }

    private File copyLenaFromResourceToFile() {
        IOGroovyMethods.withCloseable(
                PhotoCrcTest.class.classLoader.getResourceAsStream("lena.jpg")
        ) { InputStream lenaStream ->
            File lenaFile = temporaryFolder.newFile("lena.jpg")
            FileUtils.copyInputStreamToFile(lenaStream, lenaFile)
            return lenaFile
        }
    }

}

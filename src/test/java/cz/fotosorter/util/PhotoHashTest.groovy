package cz.fotosorter.util

import org.apache.commons.io.FileUtils
import org.codehaus.groovy.runtime.IOGroovyMethods
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class PhotoHashTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    void 'compute hash of lena file'() {
        File lenaFile = copyLenaFromResourceToFile()

        assert new PhotoHash(lenaFile).checksum == 3270423915
    }

    private File copyLenaFromResourceToFile() {
        IOGroovyMethods.withCloseable(
                PhotoHashTest.class.classLoader.getResourceAsStream("lena.jpg")
        ) { InputStream lenaStream ->
            File lenaFile = temporaryFolder.newFile("lena.jpg")
            FileUtils.copyInputStreamToFile(lenaStream, lenaFile)
            return lenaFile
        }
    }

}

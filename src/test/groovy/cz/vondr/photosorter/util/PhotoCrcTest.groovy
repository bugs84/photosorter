package cz.vondr.photosorter.util

import cz.vondr.photosorter.test.ClassPathCopier
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
        ClassPathCopier.copyResourceToFile("lena.jpg", temporaryFolder.newFile("lena.jpg"))
    }

}

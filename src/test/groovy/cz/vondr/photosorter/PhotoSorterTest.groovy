package cz.vondr.photosorter

import cz.vondr.photosorter.settings.FileOperation
import cz.vondr.photosorter.settings.PhotoSorterSettings
import cz.vondr.photosorter.test.ClassPathCopier
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class PhotoSorterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Test
    void 'basic sort without database test'() {
        //given
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) =
        setupPhotoSorterWithNoDatabase("cz.vondr.photosorter.tests.testphotos1")

        //when
        new PhotoSorter(settings).sort()

        //then
        def expectedDirs = ["2015_05_08", "2015_07_17"] as Set
        assert resultDir.list() as Set == expectedDirs
        assert new File(resultDir, expectedDirs[0]).list() as Set == ["2015_05_08_11-10-51__IMG_5537.JPG", "2015_05_08_12-01-46__IMG_5545.JPG"] as Set
        assert new File(resultDir, expectedDirs[1]).list() as Set == ["2015_07_17_18-09-11__IMG_5677.JPG"] as Set
    }

    @Test
    void 'basic sort with database test'() {
        //given
        def (PhotoSorterSettings settings, File resultDir) =
        setupPhotoSorterWithDatabase("cz.vondr.photosorter.tests.testphotos1")

        //when
        new PhotoSorter(settings).sort()

        //then
        def expectedDirs = ["2015_05_08", "2015_07_17"] as Set
        assert resultDir.list() as Set == expectedDirs
        assert new File(resultDir, expectedDirs[0]).list() as Set == ["2015_05_08_11-10-51__IMG_5537.JPG", "2015_05_08_12-01-46__IMG_5545.JPG"] as Set
        assert new File(resultDir, expectedDirs[1]).list() as Set == ["2015_07_17_18-09-11__IMG_5677.JPG"] as Set
    }

    @Test
    void 'If destination directory already contains file - file with new name is created'() {
        //given
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) =
        setupPhotoSorterWithNoDatabase("cz.vondr.photosorter.tests.testphotos1")

        def existingFileInResult = new File(new File(resultDir, "2015_05_08"), "2015_05_08_11-10-51__IMG_5537.JPG")
        existingFileInResult.parentFile.mkdirs()
        existingFileInResult.text = "dummy"

        //when
        new PhotoSorter(settings).sort()

        //then
        assert new File(resultDir, "2015_05_08").list() as Set == ["2015_05_08_11-10-51__IMG_5537.JPG", "2015_05_08_11-10-51__IMG_5537_2.JPG", "2015_05_08_12-01-46__IMG_5545.JPG"] as Set

    }

    //TODO TADY BY BYLO DOBRE OVERIT, ZE SE TO TAKY ZAINDEXOVALO DO DATABAZE
    @Test
    void 'if photo sorter run with INDEX option no files are moved'() {
        //given
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) =
        setupPhotoSorterWithNoDatabase("cz.vondr.photosorter.tests.testphotos1")
        settings.fileOperation = FileOperation.INDEX

        //when
        new PhotoSorter(settings).sort()

        //then
        assert filesToSortDir.list() as Set == ["IMG_5537.JPG", "IMG_5545.JPG", "IMG_5677.JPG"] as Set
        assert resultDir.listFiles().size() == 0

    }

    private List setupPhotoSorterWithNoDatabase(String resourceWithPhotos) {
        File filesToSortDir = temporaryFolder.newFolder("files-to-sort")
        File resultDir = temporaryFolder.newFolder("result-dir")
        File logFile = temporaryFolder.newFile("logFile")

        ClassPathCopier.copyPackageIntoDirectory(resourceWithPhotos, filesToSortDir)

        def settings = new PhotoSorterSettings(
                source: filesToSortDir,
                destination: resultDir,
                useDatabase: false,
                logFile: logFile,
        )
        [settings, filesToSortDir, resultDir]
    }

    List setupPhotoSorterWithDatabase(String resourceWithPhotos) {
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) =
        setupPhotoSorterWithNoDatabase(resourceWithPhotos)
        settings.databaseDirectory = temporaryFolder.newFolder("database-directory")
        [settings, resultDir]
    }
}

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
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) = setupPhotoSorterWithNoDatabase()

        new PhotoSorter(settings).sort()

        def expectedDirs = ["2015_05_08", "2015_07_17"]
        assert resultDir.list() as List == expectedDirs
        assert new File(resultDir, expectedDirs[0]).list() as List == ["2015_05_08_11-10-51__IMG_5537.JPG", "2015_05_08_12-01-46__IMG_5545.JPG"]
        assert new File(resultDir, expectedDirs[1]).list() as List == ["2015_07_17_18-09-11__IMG_5677.JPG"]
    }

    @Test
    void 'basic sort with database test'() {
        def (PhotoSorterSettings settings, File resultDir) = setupPhotoSorterWithDatabase()

        new PhotoSorter(settings).sort()

        def expectedDirs = ["2015_05_08", "2015_07_17"]
        assert resultDir.list() as List == expectedDirs
        assert new File(resultDir, expectedDirs[0]).list() as List == ["2015_05_08_11-10-51__IMG_5537.JPG", "2015_05_08_12-01-46__IMG_5545.JPG"]
        assert new File(resultDir, expectedDirs[1]).list() as List == ["2015_07_17_18-09-11__IMG_5677.JPG"]
    }

    @Test
    void 'If destination directory already contains file - file with new name is created.'() {
        //given
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) = setupPhotoSorterWithNoDatabase()

        def existingFileInResult = new File(new File(resultDir, "2015_05_08"), "2015_05_08_11-10-51__IMG_5537.JPG")
        existingFileInResult.parentFile.mkdirs()
        existingFileInResult.text = "dummy"

        //when
        new PhotoSorter(settings).sort()

        //then
        assert new File(resultDir, "2015_05_08").list() as List == ["2015_05_08_11-10-51__IMG_5537.JPG", "2015_05_08_11-10-51__IMG_5537_2.JPG", "2015_05_08_12-01-46__IMG_5545.JPG"]

    }

    //TODO TADY BY BYLO DOBRE OVERIT, ZE SE TO TAKY ZAINDEXOVALO DO DATABAZE
    @Test
    void 'if photo sorter run with INDEX option no files are moved'() {
        //given
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) = setupPhotoSorterWithNoDatabase()
        settings.fileOperation = FileOperation.INDEX

        //when
        new PhotoSorter(settings).sort()

        //then
        assert filesToSortDir.list() as List == ["IMG_5537.JPG", "IMG_5545.JPG", "IMG_5677.JPG"]
        assert resultDir.listFiles().size() == 0

    }

    private List setupPhotoSorterWithNoDatabase() {
        File filesToSortDir = temporaryFolder.newFolder("files-to-sort")
        File resultDir = temporaryFolder.newFolder("result-dir")

        ClassPathCopier.copyPackageIntoDirectory("cz.vondr.photosorter.tests.testphotos1", filesToSortDir)

        def settings = new PhotoSorterSettings(
                source: filesToSortDir,
                destination: resultDir,
        )
        [settings, filesToSortDir, resultDir]
    }

    public List setupPhotoSorterWithDatabase() {
        def (PhotoSorterSettings settings, File filesToSortDir, File resultDir) = setupPhotoSorterWithNoDatabase()
        settings.databaseDirectory = temporaryFolder.newFolder("database-directory")
        [settings, resultDir]
    }
}

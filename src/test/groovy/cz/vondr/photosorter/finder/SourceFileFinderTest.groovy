package cz.vondr.photosorter.finder

import cz.vondr.photosorter.settings.PhotoSorterSettings
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class SourceFileFinderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    List<File> foundFiles = []

    @Test
    void "find files in subdirectory"() {
        //given
        File folder = temporaryFolder.newFolder("SourceFileFinderTest")
        File pic1 = new File(folder, "picture1.jpg")
        pic1.createNewFile()
        File pic2 = new File(folder, "Picture 2.JPG")
        pic2.createNewFile()
        File pic3 = new File(folder, "Picture 3.JpG")
        pic3.createNewFile()
        File vid1 = new File(folder, "Video 1.mp4")
        vid1.createNewFile()
        File vid2 = new File(folder, "Video 2.Mp4")
        vid2.createNewFile()
        File unknown = new File(folder, "Unknown file 1.mp3")
        unknown.createNewFile()
        File subFolder = new File(folder, "subfolder 1")
        subFolder.mkdirs()
        File subPic1 = new File(subFolder, "picture 1 in subfolder.jpg")
        subPic1.createNewFile()

        SourceFileFinder finder = new SourceFileFinder(new PhotoSorterSettings(source: folder))

        //when
        finder.forEachFile { file ->
            foundFiles += file
        }

        //then
        assert foundFiles as Set == [pic1, pic2, pic3, vid1, vid2, subPic1] as Set

    }
}

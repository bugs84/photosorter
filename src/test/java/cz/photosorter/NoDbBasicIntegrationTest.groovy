package cz.photosorter
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

import java.nio.file.StandardCopyOption

import static cz.photosorter.MoveOrCopy.MOVE
import static java.nio.file.Files.copy

class NoDbBasicIntegrationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    File sourceFolder
    File destinationFolder

    @Test
    void 'basic'() {
        prepareSourceDirectory()
        createEmptyDestinationFolder()

        def photoSorter = new PhotoSorter(new PhotoSorterSettings(
                source: sourceFolder,
                destination: destinationFolder,
                moveOrCopy: MOVE,
        ))
        photoSorter.sort()

        assert sourceFolder.list().size() == 0
        assert destinationFolder.list().toList() == ["2015_05_08", "2015_07_17"]
        assert new File(destinationFolder, "2015_05_08").list().toList() == ["2015_05_08_11-10-51__IMG_5537.JPG", "2015_05_08_12-01-46__IMG_5545.JPG"]
        assert new File(destinationFolder, "2015_07_17").list().toList() == ["2015_07_17_18-09-11__IMG_5677.JPG"]


    }


    void prepareSourceDirectory() {
        sourceFolder = temporaryFolder.newFolder("source")
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/cz/photosorter/tests/testphotos1/**")
        resources.each { resource ->
            copy(resource.getInputStream(), sourceFolder.toPath().resolve(resource.filename), StandardCopyOption.REPLACE_EXISTING)
        }
    }

    void createEmptyDestinationFolder() {
        destinationFolder = temporaryFolder.newFolder("destination")
    }

}

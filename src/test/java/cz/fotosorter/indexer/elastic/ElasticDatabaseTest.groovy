package cz.fotosorter.indexer.elastic

import cz.fotosorter.indexer.api.PhotoInfo
import org.junit.After
import org.junit.Before
import org.junit.Test

class ElasticDatabaseTest {

    ElasticDatabase elastic

    @Before
    void setup() {
        elastic = new ElasticDatabase()
        elastic.start()
    }

    @After
    void cleanup() {
        elastic.stop()
    }

    @Test
    void 'photoinfo - simple insert and get'() {
        def photoInfo = createTestingPhotoInfo()

        elastic.insert(photoInfo)

        def returnedPhotoInfo = elastic.get(photoInfo.crc)
        println returnedPhotoInfo

        assert photoInfo.crc == returnedPhotoInfo.crc
        assert photoInfo.originalPath == returnedPhotoInfo.originalPath
    }

    @Test
    void 'photoinfo - get not existing photo info'() {
        def photoInfo = createTestingPhotoInfo()

        elastic.insert(photoInfo)

        def returnedPhotoInfo = elastic.get("NotExistingCrc")

        assert returnedPhotoInfo == null
    }

    public PhotoInfo createTestingPhotoInfo() {
        new PhotoInfo(
                crc: "TetsCRC",
                originalPath: "c:/tmp/original dir/Original Name.jpg",
                originalName: "Original Name.jpg",
                newPath: "c:/tmp/new dir/file.jpg",
                newName: "Original Name.jpg",
        )
    }
}

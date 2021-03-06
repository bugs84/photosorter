package cz.vondr.photosorter.date_resolver

import cz.vondr.photosorter.test.ClassPathCopier
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AllTypesDateResolverTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Test
    void "resolveDate works for classic image"() {
        //given
        File classicImage = ClassPathCopier.copyResourceToFile(
                "cz/vondr/photosorter/tests/testphotos1/IMG_5537.JPG",
                temporaryFolder.newFile("ResolveDateWorksForClassicImage.jpg")
        )

        //when
        DateResult result = new AllTypesDateResolver().resolveDate(classicImage)

        //then
        assert result.resolvedSuccessfully
        assert result.date == new GregorianCalendar(2015, 4, 8, 11, 10, 51).time
    }

    @Test
    void "resolveDate works for mp4 in LG G6 format"() {
        //given
        File file = temporaryFolder.newFile("20180817_175941.mp4")

        //when
        DateResult result = new AllTypesDateResolver().resolveDate(file)

        //then
        assert result.resolvedSuccessfully
        assert result.date == new GregorianCalendar(2018, 7, 17, 17, 59, 41).time
    }

    @Test
    void "resolveDate works for mp4 in LG G6 format and extension is case insensitive"() {
        //given
        File file = temporaryFolder.newFile("20180817_175941.Mp4")

        //when
        DateResult result = new AllTypesDateResolver().resolveDate(file)

        //then
        assert result.resolvedSuccessfully
        assert result.date == new GregorianCalendar(2018, 7, 17, 17, 59, 41).time
    }

    @Test
    void "resolveDate works for mp4 in Mi2S format"() {
        //given
        File file = temporaryFolder.newFile("VID_20170914_172650.mp4")

        //when
        DateResult result = new AllTypesDateResolver().resolveDate(file)

        //then
        assert result.resolvedSuccessfully
        assert result.date == new GregorianCalendar(2017, 8, 14, 17, 26, 50).time
    }

    @Test
    void "resolveDate works for MTS in sony file name format"() {
        //given
        File file = temporaryFolder.newFile("20140527063053.MTS")

        //when
        DateResult result = new AllTypesDateResolver().resolveDate(file)

        //then
        assert result.resolvedSuccessfully
        assert result.date == new GregorianCalendar(2014, 4, 27, 6, 30, 53).time
    }


    @Test
    void "resolveDate invalid mp4 format"() {
        //given
        File file = temporaryFolder.newFile("20180817_invalid_175941.mp4")

        //when
        DateResult result = new AllTypesDateResolver().resolveDate(file)

        //then
        assert !result.resolvedSuccessfully
        assert result.date == null
    }
}

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
    void "resolveDate works for mp4 simple file name format"() {
        //given
        File file = temporaryFolder.newFile("20180817_175941.mp4")

        //when
        DateResult result = new AllTypesDateResolver().resolveDate(file)

        //then
        assert result.resolvedSuccessfully
        assert result.date == new GregorianCalendar(2018, 7, 17, 17, 59, 41).time
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

package cz.vondr.photosorter.date_resolver

import cz.vondr.photosorter.test.ClassPathCopier
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static cz.vondr.photosorter.date_resolver.DateResolver.*

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
        Result result = new AllTypesDateResolver().resolveDate(classicImage)

        //then
        assert result.resolvedSuccessfully
        assert result.date == new GregorianCalendar(2015,4,8,11,10,51).time
    }
}

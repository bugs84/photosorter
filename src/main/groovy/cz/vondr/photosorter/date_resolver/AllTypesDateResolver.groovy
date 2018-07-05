package cz.vondr.photosorter.date_resolver

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AllTypesDateResolver implements DateResolver {

    private static final Logger logger = LoggerFactory.getLogger(AllTypesDateResolver.class)

    List<DateResolver> resolvers = [
            new JpgImageMetadataResolver()
    ]

    @Override
    DateResult resolveDate(File file) {
        for (DateResolver resolver : resolvers) {
            DateResult result = tryResolveDate(resolver, file)
            if (result.resolvedSuccessfully) {
                return result
            }
        }
        new DateResult(resolvedSuccessfully: false)
    }

    public DateResult tryResolveDate(DateResolver resolver, File file) {
        try {
            return resolver.resolveDate(file)
        } catch (Exception ex) {
            logger.warn("Resolver (${resolver.class.name}) threw exception", ex)
            return new DateResult(resolvedSuccessfully: false)
        }
    }


}

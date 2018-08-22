package cz.vondr.photosorter.date_resolver

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static cz.vondr.photosorter.date_resolver.DateResult.NOT_RESOLVED

class AllTypesDateResolver implements DateResolver {

    private static final Logger logger = LoggerFactory.getLogger(AllTypesDateResolver.class)

    private List<DateResolver> resolvers = [
            new JpgImageMetadataResolver(),
            new LgG6Mp4FileNameResolver(),
            new Mi2sMp4FileNameResolver(),
            new SonyMtsFileNameResolver(),
    ]

    @Override
    DateResult resolveDate(File file) {
        for (DateResolver resolver : resolvers) {
            DateResult result = tryResolveDate(resolver, file)
            if (result.resolvedSuccessfully) {
                return result
            }
        }
        NOT_RESOLVED
    }

    private DateResult tryResolveDate(DateResolver resolver, File file) {
        try {
            return resolver.resolveDate(file)
        } catch (Exception ex) {
            logger.warn("Resolver (${resolver.class.name}) threw exception", ex)
            return NOT_RESOLVED
        }
    }


}

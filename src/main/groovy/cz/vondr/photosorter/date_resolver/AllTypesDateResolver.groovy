package cz.vondr.photosorter.date_resolver

class AllTypesDateResolver implements DateResolver {

    List<DateResolver> resolvers = [
            new JpgImageMetadataResolver()
    ]

    @Override

    DateResult resolveDate(File file) {
        resolvers.findResult(
                new DateResult(resolvedSuccessfully: false),
                { resolver ->
                    DateResult result = resolver.resolveDate(file)
                    if (result.resolvedSuccessfully) {
                        return result
                    } else {
                        return null
                    }
                }
        )
    }


}

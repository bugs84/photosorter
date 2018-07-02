package cz.vondr.photosorter.date_resolver

import cz.vondr.photosorter.util.Utils

class AllTypesDateResolver implements DateResolver {

    @Override
    DateResolver.Result resolveDate(File file) {
        Date date = Utils.getImageDate(file)
        new DateResolver.Result(
                resolvedSuccessfully: date != null,
                date: date
        )
    }


}

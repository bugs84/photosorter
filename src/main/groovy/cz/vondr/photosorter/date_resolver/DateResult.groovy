package cz.vondr.photosorter.date_resolver

import groovy.transform.ToString

@ToString
class DateResult {
    static final DateResult NOT_RESOLVED = new DateResult(resolvedSuccessfully: false)

    boolean resolvedSuccessfully
    Date date
}

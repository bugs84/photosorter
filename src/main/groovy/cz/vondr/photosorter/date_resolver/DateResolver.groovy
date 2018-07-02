package cz.vondr.photosorter.date_resolver

interface DateResolver {
    static class Result {
        boolean resolvedSuccessfully
        Date date
    }

    Result resolveDate(File file)
}
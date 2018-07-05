package cz.vondr.photosorter.util

class Utils {
    static String getFolderName(Date date) {
        def c = Calendar.getInstance()
        c.setTime(date)
        def year = c.get(Calendar.YEAR)
        def month = c.get(Calendar.MONTH) + 1
        def day = c.get(Calendar.DAY_OF_MONTH)
        String.format("%d_%02d_%02d", year, month, day)
    }

}

package cz.vondr.photosorter

import cz.vondr.photosorter.settings.PhotoSorterSettings

import static cz.vondr.photosorter.settings.FileOperation.*

class MainRun {
    private void start() {
        PhotoSorterSettings settings = new PhotoSorterSettings(
                //        source: new File("c:/foto/AANikkonTransfer/toSort/"),
                //        source: new File("d:/foto/AA_Mi2S_Transfer/Camera/"),
//                source: new File("d:/foto/AANikkonTransfer/foto_Nikon_Besidka/102D3200/"),
//                source: new File("d:\\foto\\AA_G6_Spanelsko\\download_vse"),
                source: new File("d:\\foto\\AA_NIKON\\foto_Nikon_2018_03\\"),
                //        source: new File("c:/foto/AA_Canon_G12_Transfer/"),
                destination: new File("d:/foto"),
                fileOperation: MOVE,

                useDatabase: true,

//                File source
//                File destination
//
//                /** If files should be copied, moved, or indexed.<br>
//                 * Index mean, that file is only added into database and nothing else is done with file.<br>
//                 *  Default is COPY */
//                FileOperation fileOperation = COPY
//
//                /** If database should be used.<br>
//                 * Into database are stored information about processed files.<br>
//                 * Thanks to it same file will not be copied multiple times.<br>
//                 *
//                 * Default value is true */
//                Boolean useDatabase = true
//                /** Directory where will be stored information about files, which was already processed.<br>
//                 * Thanks to it same file will not be copied multiple times.<br>
//                 * If null - database directory will be in "${destination}/.photosorter/database/"<br>
//                 * Default value is null */
//                File databaseDirectory = null
//
//                /** Log File location.<br>
//                 * If null - log file will be in "${destination}/.photosorter/logs/PhotoSorter.log"<br>
//                 * Default value is null */
//                File logFile = null


        )
        PhotoSorter photoSorter = PhotoSorterFactory.createPhotoSorter(settings)
        photoSorter.sort()
    }

    static void main(String[] args) {
        MainRun app = new MainRun();
        app.start();
    }
}

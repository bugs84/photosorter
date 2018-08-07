package cz.vondr.photosorter

import cz.vondr.photosorter.settings.PhotoSorterSettings

import static cz.vondr.photosorter.settings.FileOperation.*

class MainRun {
    private void start() {
        def photoSorter = new PhotoSorter(new PhotoSorterSettings(
                //        source: new File("c:/foto/AANikkonTransfer/toSort/"),
                //        source: new File("d:/foto/AA_Mi2S_Transfer/Camera/"),
//                source: new File("d:/foto/AANikkonTransfer/foto_Nikon_Besidka/102D3200/"),
//                source: new File("d:\\foto\\AA_G6_Spanelsko\\download_vse"),
                source: new File("d:\\foto\\AA_NIKON\\foto_Nikon_2018_03\\"),
                //        source: new File("c:/foto/AA_Canon_G12_Transfer/"),
                destination: new File("d:/foto"),
                fileOperation: MOVE,

                //TODO configure use database true/false  a ne ten muj vymysl s nulem
                //TODO configure log directory
                databaseDirectory: new File("d:/foto/.photosorter/database/")
        ))
        photoSorter.sort()
    }

    static void main(String[] args) {
        MainRun app = new MainRun();
        app.start();
    }
}

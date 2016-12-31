package cz.vondr.photosorter

import cz.vondr.photosorter.settings.PhotoSorterSettings

import static cz.vondr.photosorter.settings.FileOperation.NOTHING

class MainRun {
    private void start() {
        def photoSorter = new PhotoSorter(new PhotoSorterSettings(
        //        source: new File("c:/foto/AANikkonTransfer/toSort/"),
                source: new File("c:/foto/AA_Mi2S_Transfer/Camera/"),
        //        source: new File("c:/foto/AA_Canon_G12_Transfer/"),
                destination: new File("c:/fotoDELETE"),
                fileOperation: NOTHING,
                databaseDirectory: new File("c:/foto/.photosorter/database/")
        ))
        photoSorter.sort()
    }

    public static void main(String[] args) {
        MainRun app = new MainRun();
        app.start();
    }
}

package cz.vondr.photosorter

import cz.vondr.photosorter.settings.PhotoSorterSettings

class Main {

    static void writeHelp() {
        println '''Two arguments are required!

Usage:
Main <source folder> <destination folder>

Short description:
This program take all photos from 'source folder' and move them into 'destination folder/YYYY_mm_DD' folder.
'''
    }

    static void main(String[] args) {
        if (args == null || args.length != 2) {
            writeHelp()
            return
        }

        new PhotoSorter(new PhotoSorterSettings(
                source: new File(args[0]),
                destination: new File(args[1]),
        )).sort()
    }
}

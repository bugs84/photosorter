package cz.fotosorter

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
        if(args == null || args.length != 2) {
            writeHelp()
            return
        }

        File source = new File(args[0])
        File destination = new File(args[1])

        new PhotoSorter().moveFiles(source, destination)
    }
}

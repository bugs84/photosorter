package cz.photosorter.indexer.api

trait Database {

    abstract void start()

    abstract void stop()

    abstract void insert(PhotoInfo photoInfo)

    /** return null if photo no such crc found */
    abstract PhotoInfo get(String crc)

    abstract boolean contains(String crc)
}
package cz.vondr.photosorter.indexer.dummy

import cz.vondr.photosorter.indexer.api.Database
import cz.vondr.photosorter.indexer.api.PhotoInfo

class DummyDatabase implements Database{
    @Override
    void start() {
    }

    @Override
    void stop() {
    }

    @Override
    void insert(PhotoInfo photoInfo) {
    }

    @Override
    PhotoInfo get(String crc) {
    }

    @Override
    boolean contains(String crc) {
        false
    }
}

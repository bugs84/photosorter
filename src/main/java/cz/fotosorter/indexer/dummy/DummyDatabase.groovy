package cz.fotosorter.indexer.dummy

import cz.fotosorter.indexer.api.Database
import cz.fotosorter.indexer.api.PhotoInfo

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

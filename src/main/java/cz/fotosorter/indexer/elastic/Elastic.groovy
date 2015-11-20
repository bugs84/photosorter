package cz.fotosorter.indexer.elastic

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import cz.fotosorter.indexer.api.PhotoInfo
import org.elasticsearch.action.get.GetRequestBuilder
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.Client
import org.elasticsearch.node.Node
import org.elasticsearch.node.NodeBuilder

class Elastic {

//    "c:/foto/.photosorter/elastic-database"
    private String databaseDirectory = "target/elastic-database"

    private String indexName = "photo-index"
    private String type = "photo-info"

    private Node node
    private Client client
    private ObjectWriter jsonWriter
    private ObjectReader jsonReader
    {
        jsonWriter = new ObjectMapper().writerFor(PhotoInfo.class)
        jsonReader = new ObjectMapper().readerFor(PhotoInfo.class)
    }

    String getDatabaseDirectory() {
        databaseDirectory
    }

    Elastic setDatabaseDirectory(String databaseDirectory) {
        this.databaseDirectory = databaseDirectory
        this
    }

    public void start() {
        def builder = NodeBuilder.nodeBuilder()
        builder.getSettings().put("path.data", databaseDirectory)
        node = builder.node()
        client = this.node.client()
    }

    public void stop() {
        this.node.close()
    }

    public void insert(PhotoInfo photoInfo) {
        IndexRequest indexRequest = new IndexRequest(indexName, type, photoInfo.crc);
        indexRequest.source(jsonWriter.writeValueAsString(photoInfo))
        IndexResponse response = this.client.index(indexRequest).actionGet();
    }

    /** return null if photo no such crc found */
    public PhotoInfo get(String crc) {
        GetRequestBuilder getRequestBuilder = client.prepareGet(indexName, type, crc);
        GetResponse getResponse = getRequestBuilder.execute().actionGet();
        def photoInfoString = getResponse.getSourceAsString()
        if (photoInfoString == null) {
            return null
        }
        PhotoInfo photoInfo = jsonReader.readValue(photoInfoString)
        return photoInfo
    }

    private void test() {
        start()


        this.client.index(new IndexRequest())
        // on shutdown


        def photoInfo = new PhotoInfo(
                crc: "TetsCRC",
                originalPath: "c:/tmp/original dir/Original Name.jpg",
                originalName: "Original Name.jpg",
                newPath: "c:/tmp/new dir/file.jpg",
                newName: "Original Name.jpg",
        )

        //insert
        IndexRequest indexRequest = new IndexRequest(indexName, type, photoInfo.crc);
        indexRequest.source(jsonWriter.writeValueAsString(photoInfo))
        IndexResponse response = this.client.index(indexRequest).actionGet();
//        println response

        //Get document - whole
        GetRequestBuilder getRequestBuilder = client.prepareGet(indexName, type, photoInfo.crc);
        GetResponse getResponse = getRequestBuilder.execute().actionGet();
        println "WHOLE object\n" + getResponse.getSourceAsString()

        //Get document - one field
        GetRequestBuilder getRequestBuilder2 = client.prepareGet(indexName, type, photoInfo.crc);
        getRequestBuilder2.setFields("originalPath");
        GetResponse getResponse2 = getRequestBuilder2.execute().actionGet();
        String gettedField2 = getResponse2.getField("originalPath").getValue().toString();
        println "One Field\n" + gettedField2 + "\n\n"



        stop();
    }

    public static void main(String[] args) {
        new Elastic().test()
    }
}

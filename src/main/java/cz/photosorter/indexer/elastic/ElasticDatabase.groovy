package cz.photosorter.indexer.elastic

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import cz.photosorter.indexer.api.Database
import cz.photosorter.indexer.api.PhotoInfo
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse
import org.elasticsearch.action.get.GetRequestBuilder
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.Client
import org.elasticsearch.node.Node
import org.elasticsearch.node.NodeBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ElasticDatabase implements Database {

    private static final Logger logger = LoggerFactory.getLogger(ElasticDatabase.class)

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

    ElasticDatabase setDatabaseDirectory(String databaseDirectory) {
        this.databaseDirectory = databaseDirectory
        this
    }

    @Override
    public void start() {
        def builder = NodeBuilder.nodeBuilder()
        builder.getSettings().put("path.data", databaseDirectory)
        node = builder.node()
        client = this.node.client()

        prepareIndex()
    }

    private void prepareIndex() {
        //this is needed, because I am not able setup "ignore_unavailable" in get request
        IndicesExistsResponse existsResponse = client.admin().indices().prepareExists(indexName).execute().actionGet()
        if (!existsResponse.exists) {
            logger.info "Index doesn't exists - going to create new."
            createIndex()
        } else {
            logger.info "Ok - Index already exists."
        }
    }

    private void createIndex() {
        //inserting fake photo info to create index, because next line doesn't work well
        //client.admin().indices().prepareCreate(indexName).execute().actionGet()
        insert(new PhotoInfo(crc: "fakeObjectToCreateIndex"))
    }

    @Override
    public void stop() {
        this.node.close()
    }

    @Override
    public void insert(PhotoInfo photoInfo) {
        IndexRequest indexRequest = new IndexRequest(indexName, type, photoInfo.crc);
        indexRequest.source(jsonWriter.writeValueAsString(photoInfo))
        IndexResponse response = this.client.index(indexRequest).actionGet();
    }

    @Override
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

    @Override
    boolean contains(String crc) {
        get(crc) != null
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

        //Get document - whole
        GetRequestBuilder getRequestBuilder = client.prepareGet(indexName, type, photoInfo.crc);
        GetResponse getResponse = getRequestBuilder.execute().actionGet();
        logger.info "WHOLE object\n" + getResponse.getSourceAsString()

        //Get document - one field
        GetRequestBuilder getRequestBuilder2 = client.prepareGet(indexName, type, photoInfo.crc);
        getRequestBuilder2.setFields("originalPath");
        GetResponse getResponse2 = getRequestBuilder2.execute().actionGet();
        String gettedField2 = getResponse2.getField("originalPath").getValue().toString();
        logger.info "One Field\n" + gettedField2 + "\n\n"



        stop();
    }

    public static void main(String[] args) {
        new ElasticDatabase().test()
    }
}
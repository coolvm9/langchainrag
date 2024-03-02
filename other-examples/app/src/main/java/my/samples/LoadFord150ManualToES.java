package my.samples;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadFord150ManualToES {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) {

        EmbeddingStore<TextSegment> embeddingStore = ElasticsearchEmbeddingStore.builder()
                .serverUrl("http://localhost:9200")
                .indexName("car-warranty-guide-embeddings")
                .dimension(384)
                .build();

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        Path filePath = toPath("example-files/2025_US_F150_Warranty_Guide_ENG_V1.pdf");

        Document document = FileSystemDocumentLoader.loadDocument(filePath, new ApachePdfBoxDocumentParser());

        document.metadata().add("fileName", filePath.getFileName().toString());
        document.metadata().add("filePath", filePath.toString());
        document.metadata().add("company", "FORD");
        document.metadata().add("product", "F150");
        document.metadata().add("language", "ENG");
        document.metadata().add("version", "V1");
        document.metadata().add("year", "2025");
        document.metadata().add("type", "Warranty Guide");
        document.metadata().add("country", "US");
        document.metadata().add("category", "Automotive");
        ingestor.ingest(document);
        System.out.println(ANSI_GREEN + "Document ingested successfully" + ANSI_RESET);


    }

    private static Path toPath(String fileName) {
        try {
            // Corrected path assuming files are in src/main/resources/example-files
            URL fileUrl = LoadFord150ManualToES.class.getClassLoader().getResource( fileName);
            if (fileUrl == null) {
                throw new RuntimeException("Resource not found: " + fileName);
            }
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to resolve URI for: " + fileName, e);
        }
    }
}

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

public class LoadFINRARuletoES {


    public static void main(String[] args) {

        EmbeddingStore<TextSegment> embeddingStore = ElasticsearchEmbeddingStore.builder()
                .serverUrl("http://localhost:9200")
                .indexName("finra-rules-embeddings")
                .dimension(384)
                .build();
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        Path filePath = toPath("example-files/FINRARULES.pdf");

        Document document = FileSystemDocumentLoader.loadDocument(filePath, new ApachePdfBoxDocumentParser());

        document.metadata().add("fileName", filePath.getFileName().toString());
        document.metadata().add("filePath", filePath.toString());
        document.metadata().add("source", "FINRA");
        document.metadata().add("category", "FINANCE");

        ingestor.ingest(document);
        System.out.println(  "Document ingested successfully" );

    }

    private static Path toPath(String fileName) {
        try {
            // Corrected path assuming files are in src/main/resources/example-files
            URL fileUrl = LoadFINRARuletoES.class.getClassLoader().getResource( fileName);
            if (fileUrl == null) {
                throw new RuntimeException("Resource not found: " + fileName);
            }
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to resolve URI for: " + fileName, e);
        }
    }
}

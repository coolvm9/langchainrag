package my.samples;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QueryEmbeddingManualExample {
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





        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your query (or type 'exit' to quit):");

            // Wait for the user to input a query
            String query = scanner.nextLine();

            // Check if the user wants to exit the program
            if ("exit".equalsIgnoreCase(query)) {
                System.out.println("Exiting program.");
                break;
            }

            // Who Pays For Warranty Repairs?
            // What is the warranty period?
            // What is the warranty period for the powertrain?
            // What is the warranty period for the powertrain?


            // Process the query and get an answer

            Embedding queryEmbedding = embeddingModel.embed(query).content();

            List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding,5 );
            System.out.println("Start ---------   Matching Context from Document");
            List<String> answers = new ArrayList<>();
            for (EmbeddingMatch<TextSegment> match : relevant) {
                System.out.println(match.score());

                answers.add(match.embedded().text());
                System.out.println(ANSI_GREEN+match.embedded().text()+ANSI_RESET);
                System.out.println("");
            }
            System.out.println("End ---------   Matching Context from Document");
            if(!answers.isEmpty()){
                try {
                    System.out.println(ANSI_YELLOW+ RestClient.getAnswer(query, answers) + ANSI_RESET);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        // Close the scanner
        scanner.close();


        // In-memory embedding store can be serialized and deserialized to/from JSON
         String serializedStore = ((InMemoryEmbeddingStore)embeddingStore).serializeToJson();
        System.out.println(serializedStore);

        // InMemoryEmbeddingStore<TextSegment> deserializedStore = InMemoryEmbeddingStore.fromJson(serializedStore);

        // In-memory embedding store can be serialized and deserialized to/from file
        // String filePath = "/home/me/embedding.store";
        // embeddingStore.serializeToFile(filePath);
        // InMemoryEmbeddingStore<TextSegment> deserializedStore = InMemoryEmbeddingStore.fromFile(filePath);
    }

    private static Path toPath(String fileName) {
        try {
            // Corrected path assuming files are in src/main/resources/example-files
            URL fileUrl = QueryEmbeddingManualExample.class.getClassLoader().getResource( fileName);
            if (fileUrl == null) {
                throw new RuntimeException("Resource not found: " + fileName);
            }
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to resolve URI for: " + fileName, e);
        }
    }
}

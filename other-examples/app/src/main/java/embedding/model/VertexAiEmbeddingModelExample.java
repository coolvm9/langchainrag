package embedding.model;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;

public class VertexAiEmbeddingModelExample {

    public static void main(String[] args) {

        EmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
                .endpoint("us-central1-aiplatform.googleapis.com:443")
                .project("neat-vent-381323")
                .location("us-central1")
                .publisher("google")
                .modelName("textembedding-gecko@002")
                .build();

        Response<Embedding> response = embeddingModel.embed("Hello, how are you?");
        System.out.println(response);
    }
}

package my.samples;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentLoaderExamples {

    static class Load_Pdf_From_File_System_Example {

        public static void main(String[] args) {
            try {
                // Load a PDF from the file system


                Path filePath = toPath("example-files/2025_US_F150_Warranty_Guide_ENG_V1.pdf");

                Document document = FileSystemDocumentLoader.loadDocument(filePath, new ApachePdfBoxDocumentParser());

                System.out.println(document);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
            }

        }
    }

    static class Load_Docx_From_File_System_Example {

        public static void main(String[] args) {
            Path filePath = toPath("example-files/story-about-happy-carrot.docx");

            Document document = FileSystemDocumentLoader.loadDocument(filePath, new ApachePoiDocumentParser());

            System.out.println(document);
        }
    }

    private static Path toPath(String fileName) {
        try {
            // Corrected path assuming files are in src/main/resources/example-files
            URL fileUrl = DocumentLoaderExamples.class.getClassLoader().getResource( fileName);
            if (fileUrl == null) {
                throw new RuntimeException("Resource not found: " + fileName);
            }
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to resolve URI for: " + fileName, e);
        }
    }

}

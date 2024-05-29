import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class PythonIntegration {
    public static void main(String[] args) {
        try {
            String relativePath = "embeddings_generator.py";
            String pythonScriptPath = Paths.get(relativePath).toString();
            String sentences = "[\"This is a sentence\", \"This is another sentence\"]";  // Replace with your sentences
            String[] cmd = new String[3];
            cmd[0] = "python";
            cmd[1] = pythonScriptPath;
            cmd[2] = sentences;

            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String line;
            System.out.println("Here is the standard output of the command:\n");
            while ((line = stdInput.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println("Here is the standard error of the command (if any):\n");
            while ((line = stdError.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package org.pianomyn.gred.reading;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BufferedLineReader implements LineReader {
    private Path filePath;
    private BufferedReader br;
    public BufferedLineReader(Path filePath) {
        this.filePath = filePath;
        try {
            this.br = Files.newBufferedReader(filePath);
        } catch(MalformedInputException e) {

        } catch(IOException e) {
            // File doesnt exist?
        }

    }

    public String readLine() throws IOException{
        return this.br.readLine();
    }

    public void close() throws IOException {
        this.br.close();
    }
}

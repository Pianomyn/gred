package org.pianomyn.gred.reading;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReaderFacade {
    private LineReader reader;

    public FileReaderFacade(LineReader reader) {
        this.reader = reader;
    }

    public String readLine() throws IOException {
        return this.reader.readLine();
    }

    public void close() throws IOException {
        this.reader.close();
    }
}

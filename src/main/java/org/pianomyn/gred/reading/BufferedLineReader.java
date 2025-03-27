package org.pianomyn.gred.reading;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BufferedLineReader extends LineReader {
  private BufferedReader br;

  public BufferedLineReader(Path filePath) throws IOException {
    super(filePath);

    try {
      this.br = Files.newBufferedReader(this.filePath);
    } catch (MalformedInputException e) {
      // System.out.println
      // System.err.println("The file " + filePath.toString() + " is a binary file.");
    } catch (IOException e) {
      // System.out.println("Error reading file " + e.toString());
      // System.exit(1);
      // System.err.println("Error reading file " + e.toString());
      throw new IOException("Error reading file " + e.toString());
      // throw new FileNotFoundException(filePath.toString());
    }
  }

  public String readLine() throws IOException {
    return this.br.readLine();
  }

  public void close() throws IOException {
    this.br.close();
  }
}

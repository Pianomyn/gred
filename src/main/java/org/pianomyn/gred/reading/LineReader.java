package org.pianomyn.gred.reading;

import java.io.IOException;
import java.nio.file.Path;

public abstract class LineReader {
  protected Path filePath;

  public LineReader(Path filePath) throws IOException {
    this.filePath = filePath;
  }

  public Path getFilePath() {
    return this.filePath;
  }

  public abstract String readLine() throws IOException;

  public abstract void close() throws IOException;
}

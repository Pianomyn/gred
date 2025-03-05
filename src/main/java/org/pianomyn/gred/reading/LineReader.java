package org.pianomyn.gred.reading;

import java.io.IOException;

public interface LineReader {
  String readLine() throws IOException;

  void close() throws IOException;
}

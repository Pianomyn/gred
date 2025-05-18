package org.pianomyn.gred.orchestration.consumer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface Consumer {
    Map<String, List<List<Integer>>> consume(Queue<Path> paths);
}

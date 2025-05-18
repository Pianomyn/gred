package org.pianomyn.gred.orchestration.producer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface Producer {
    Queue<Path> produce(Path root);
}

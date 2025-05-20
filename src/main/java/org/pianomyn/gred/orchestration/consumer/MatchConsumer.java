package org.pianomyn.gred.orchestration.consumer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.orchestration.PoisonPill;
import org.pianomyn.gred.reading.BufferedLineReader;
import org.pianomyn.gred.reading.LineReader;

public class MatchConsumer implements Consumer, Runnable {
  MatchingAlgorithm matchingAlgorithm;
  Queue<Path> filePaths;
  Map<String, List<List<Integer>>> matches;
  int numConsumers;

  public MatchConsumer(
      Queue<Path> filePaths,
      MatchingAlgorithm matchingAlgorithm,
      Map<String, List<List<Integer>>> matches,
      int numConsumers) {
    this.matchingAlgorithm = matchingAlgorithm;
    this.filePaths = filePaths;
    this.matches = matches;
    this.numConsumers = numConsumers;
  }

  @Override
  public void consume() {
    while (true) {
      if (filePaths.isEmpty()) {
        continue;
      }

      Path path = null;
      if (numConsumers == 1) {
        path = this.filePaths.poll();
      } else {
        // TODO: Review and possibly clean up structures
        try {
          path = ((BlockingQueue<Path>) this.filePaths).take();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }

      LineReader reader = null;
      try {
        if (path.equals(PoisonPill.POISON_PILL)) {
          return;
        }
        reader = new BufferedLineReader(path);
      } catch (IOException e) {
        System.out.println(e);
      }
      this.matchingAlgorithm.setReader(reader);
      this.matchingAlgorithm.findMatches();
    }
  }

  @Override
  public void run() {
    this.consume();
  }
}

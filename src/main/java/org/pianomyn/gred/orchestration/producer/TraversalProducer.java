package org.pianomyn.gred.orchestration.producer;

import java.nio.file.Path;
import java.util.Queue;
import org.pianomyn.gred.orchestration.PoisonPill;
import org.pianomyn.gred.traversal.BFS;

public class TraversalProducer implements Producer, Runnable {
  BFS bfs;
  int numConsumers;

  public TraversalProducer(Queue<Path> filePaths, Path root, int numConsumers) {
    this.bfs = new BFS(root, filePaths);
    this.numConsumers = numConsumers;
  }

  @Override
  public void produce() {
    this.bfs.traverse();
    for (int i = 0; i < numConsumers; i++) {
      this.bfs.getFilePaths().add(PoisonPill.POISON_PILL);
    }
  }

  @Override
  public void run() {
    this.bfs.traverse();
  }
}

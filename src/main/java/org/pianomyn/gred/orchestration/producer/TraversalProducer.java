package org.pianomyn.gred.orchestration.producer;

import java.nio.file.Path;
import java.util.Queue;
import org.pianomyn.gred.traversal.BFS;

public class TraversalProducer implements Producer, Runnable {
  BFS bfs;

  public TraversalProducer(Queue<Path> filePaths, Path root, int numConsumers) {
    this.bfs = new BFS(root, filePaths);
  }

  @Override
  public void produce() {
    this.bfs.traverse();
  }

  @Override
  public void run() {
    this.bfs.traverse();
  }
}

package org.pianomyn.gred.orchestration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import org.pianomyn.gred.matching.AlgorithmType;
import org.pianomyn.gred.matching.MatchingAlgorithm;
import org.pianomyn.gred.matching.MatchingAlgorithmFactory;
import org.pianomyn.gred.orchestration.consumer.Consumer;
import org.pianomyn.gred.orchestration.consumer.MatchConsumer;
import org.pianomyn.gred.orchestration.producer.Producer;
import org.pianomyn.gred.orchestration.producer.TraversalProducer;
import org.pianomyn.gred.reading.BufferedLineReader;
import org.pianomyn.gred.reading.LineReader;

public class Orchestrator {
  Path directoryPath;
  String pattern;
  AlgorithmType algorithmType;
  Queue<Path> filePaths;
  Map<String, List<List<Integer>>> matches;
  int numConsumers = 1;

  public Orchestrator(
      Path directoryPath, String pattern, AlgorithmType algorithmType, int numConsumers) {
    this.directoryPath = directoryPath;
    this.pattern = pattern;
    this.algorithmType = algorithmType;
    // Blocking queue to not waste CPU cycles while waiting for producer.
    this.filePaths = numConsumers == 1 ? new LinkedList<>() : new LinkedBlockingQueue<>();
    this.matches = numConsumers == 1 ? new HashMap<>() : new ConcurrentHashMap<>();
    this.numConsumers = numConsumers;
    System.out.println(numConsumers);
  }

  // Getters
  public Map<String, List<List<Integer>>> getMatches() {
    return this.matches;
  }

  public Path getDirectoryPath() {
    return this.directoryPath;
  }

  // Setters
  public void setAlgorithmType(AlgorithmType algorithmType) {
    this.algorithmType = algorithmType;
  }

  public void setDirectoryPath(Path directoryPath) {
    this.directoryPath = directoryPath;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setNumConsumers(int numConsumers) {
    this.numConsumers = numConsumers;
  }

  public void findMatches() throws IOException {
    if (this.numConsumers == 1) {
      this.findMatchesSequentially();
    } else {
      this.findMatchesConcurrently();
    }
  }

  public void findMatchesSequentially() throws IOException {
    LineReader reader = null;
    try {
      reader = new BufferedLineReader(directoryPath);
    } catch (IOException e) {
      throw e;
    }
    MatchingAlgorithm matchingAlgorithm =
        MatchingAlgorithmFactory.create(this.matches, this.algorithmType, reader, this.pattern);

    Producer p = new TraversalProducer(this.filePaths, this.directoryPath, this.numConsumers);
    Consumer c =
        new MatchConsumer(this.filePaths, matchingAlgorithm, this.matches, this.numConsumers);

    p.produce();
    c.consume();
  }

  public void findMatchesConcurrently() throws IOException {
    Thread producerThread =
        new Thread(new TraversalProducer(this.filePaths, this.directoryPath, this.numConsumers));
    producerThread.start();

    Thread[] consumers = new Thread[this.numConsumers];
    for (int i = 0; i < this.numConsumers; i++) {
      LineReader reader = null;
      try {
        reader = new BufferedLineReader(directoryPath);
      } catch (IOException e) {
        throw e;
      }
      MatchingAlgorithm matchingAlgorithm =
          MatchingAlgorithmFactory.create(this.matches, this.algorithmType, reader, this.pattern);
      consumers[i] =
          new Thread(
              new MatchConsumer(
                  this.filePaths, matchingAlgorithm, this.matches, this.numConsumers));
      consumers[i].start();
    }

    try {
      producerThread.join();
      for (int i = 0; i < this.numConsumers; i++) {
        this.filePaths.offer(PoisonPill.POISON_PILL);
      }
      for (Thread consumer : consumers) {
        consumer.join();
      }
    } catch (InterruptedException e) {
      System.out.println("Main thread interrupted");
      Thread.currentThread().interrupt();
    }
  }
}

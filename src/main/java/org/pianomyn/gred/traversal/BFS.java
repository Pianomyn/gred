package org.pianomyn.gred.traversal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.LinkedList;
import java.util.stream.Stream;

public class BFS {
  private Queue<Path> bfsQueue;
  private Path root;

  public BFS(Path root) {
    this.root = root;
    this.bfsQueue = new LinkedList<>();
  }

  // Getters
  public Path getRoot() {
    return this.root;
  }

  // Setters
  public void setRoot(Path newRoot) {
    this.root = newRoot;
  }

  // Instance methods
  public Queue<Path> traverse() {
    Queue<Path> filePaths = new LinkedList<Path>();
    if (Files.exists(this.root)) {
      this.bfsQueue.offer(this.root);
    }

    while (!this.bfsQueue.isEmpty()) {
      this.exploreCurrentDepth(filePaths);
    }
    return filePaths;
  }

  private void exploreCurrentDepth(Queue<Path> filePaths) {
    int n = this.bfsQueue.size();
    Path currentPath;
    while (n > 0) {
      currentPath = this.bfsQueue.poll();
      this.enqueueChildren(filePaths, currentPath);
      n--;
    }
  }

  private void enqueueChildren(Queue<Path> filePaths, Path currentPath) {
    if (Files.isDirectory(currentPath)) {
      try {
        Stream<Path> children = Files.list(currentPath);
        children.forEach(
            child -> {
              this.bfsQueue.offer(child);
            });
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (Files.isRegularFile(currentPath)) {
      filePaths.offer(currentPath);
    }
  }

  public static void main(String[] args) {
    BFS t = new BFS(Paths.get("/home/andi/mydir"));
    System.out.println("Started");
    Queue<Path> filePaths = t.traverse();
    while (!filePaths.isEmpty()) {
      System.out.println(filePaths.poll().toString());
    }
    System.out.println("Ended");
  }
}

package org.pianomyn.gred.traversal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

public class BFS {
  private Queue<Path> frontier;
  private Queue<Path> filePaths;
  private Path root;

  public BFS(Path root, Queue<Path> filePaths) {
    this.root = root;
    this.filePaths = filePaths;
    this.frontier = new LinkedList<>();
  }

  // Getters
  public Path getRoot() {
    return this.root;
  }

  public Queue<Path> getFilePaths() {
    return this.filePaths;
  }

  // Setters
  public void setRoot(Path newRoot) {
    this.root = newRoot;
  }

  // Instance methods
  public void traverse() {
    if (Files.exists(this.root)) {
      this.frontier.offer(this.root);
    }

    while (!this.frontier.isEmpty()) {
      this.exploreCurrentDepth();
    }
  }

  private void exploreCurrentDepth() {
    int n = this.frontier.size();
    Path currentPath;
    while (n > 0) {
      currentPath = this.frontier.poll();
      enqueueChildren(currentPath);
      n--;
    }
  }

  private void enqueueChildren(Path currentPath) {
    if (Files.isDirectory(currentPath)) {
      try {
        Stream<Path> children = Files.list(currentPath);
        children.forEach(
            child -> {
              this.frontier.offer(child);
            });
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (Files.isRegularFile(currentPath)) {
      this.filePaths.offer(currentPath);
    }
  }

  public static void main(String[] args) {
    BFS t = new BFS(Paths.get("/home/andi/mydir"), new LinkedList<>());
    System.out.println("Started");
    t.traverse();
    Queue<Path> filePaths = t.getFilePaths();
    while (!filePaths.isEmpty()) {
      System.out.println(filePaths.poll().toString());
    }
    System.out.println("Ended");
  }
}

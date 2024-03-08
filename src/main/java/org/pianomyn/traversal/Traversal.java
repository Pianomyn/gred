package org.pianomyn.traversal;

import java.io.File;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class Traversal {
  private Queue<String> files;
  private String root;

  public Traversal(String root, Queue<String> files) {
    this.root = root;
    this.files = files;
  }

  public String getRoot() { return this.root; }
  public Queue<String> getFiles() { return this.files; }

  public void traverse() {
    File parent = new File(this.root);
    File[] children = parent.listFiles();

    if (files != null) {
      for (File c : children) {
        if (c.isDirectory()) {
          traverse();
        } else {
          this.files.add(c.getName());
        }
      }
    }
  }

  public static void main(String[] args) {
    System.out.println("Hello world");
    Traversal t =
        new Traversal("~/testtraversaldir/", new LinkedList<String>());
    t.traverse();
    while(t.getFiles().size() > 0) {
      System.out.println(t.getFiles().poll());
    }
  }
}

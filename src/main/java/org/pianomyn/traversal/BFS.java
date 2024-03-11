package org.pianomyn.traversal;

import java.io.File;
import java.util.Queue;
import java.util.LinkedList;

public class BFS {
    private Queue<String> bfsQueue;
    private Queue<String> filePaths;
    private String root;

    public BFS(String root) {
        this.root = root;
        this.bfsQueue = new LinkedList<>();
        this.filePaths = new LinkedList<>();
    }

    // Getters
    public String getRoot() {
        return this.root;
    }

    public Queue<String> getFilePaths() {
        return this.filePaths;
    }

    // Setters
    public void setRoot(String newRoot) {
        this.root = newRoot;
    }

    // Instance methods
    public void traverse(String root) {
        this.clearFilePaths();
        File currentFile = new File(this.root);
        if (currentFile.exists()) {
            this.bfsQueue.offer(this.root);
        }

        while (!this.bfsQueue.isEmpty()) {
            this.exploreCurrentDepth();
        }
    }

    private void exploreCurrentDepth() {
        int n = this.bfsQueue.size();
        String currentName;
        File currentFile;
        while (n > 0) {
            currentName = this.bfsQueue.poll();
            this.enqueueChildren(currentName);
            n--;
        }
    }

    private void enqueueChildren(String currentName) {
        File currentFile = new File(currentName);
        if (currentFile.isDirectory()) {
            for (File child : currentFile.listFiles()) {
                this.bfsQueue.offer(currentName + "/" + child.getName());
            }
        } else if (currentFile.isFile()) {
            this.filePaths.offer(currentName);
        }
    }

    public void clearFilePaths() {
        this.filePaths.clear();
    }

    public static void main(String[] args) {
        BFS t = new BFS("/home/andi/mydir");
        System.out.println("Started");
        t.traverse(t.getRoot());
        while (!t.getFilePaths().isEmpty()) {
            System.out.println(t.getFilePaths().poll());
        }
        System.out.println("Ended");
    }
}

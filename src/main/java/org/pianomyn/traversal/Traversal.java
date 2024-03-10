package org.pianomyn.traversal;

import java.io.File;
import java.util.Queue;
import java.util.LinkedList;

public class Traversal {
    private Queue<String> bfsQueue;
    private Queue<String> result;
    private String root;

    public Traversal(String root) {
        this.root = root;
        this.bfsQueue = new LinkedList<>();
        this.result = new LinkedList<>();
    }

    // Getters
    public String getRoot() {
        return this.root;
    }

    public Queue<String> getResult() {
        return this.result;
    }

    // Setters
    public void setRoot(String newRoot) {
        this.root = newRoot;
    }

    // Instance methods
    public void traverse(String root) {
        this.result.clear();
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
            this.enbfsQueueChildren(currentName);
            n--;
        }
    }

    private void enbfsQueueChildren(String currentName) {
        File currentFile = new File(currentName);
        if (currentFile.isDirectory()) {
            for (File child : currentFile.listFiles()) {
                this.bfsQueue.offer(currentName + "/" + child.getName());
            }
        } else if (currentFile.isFile()) {
            this.result.offer(currentName);
        }
    }

    public static void main(String[] args) {
        Traversal t = new Traversal("/home/andi/mydir");
        System.out.println("Started");
        t.traverse(t.getRoot());
        while (!t.getResult().isEmpty()) {
            System.out.println(t.getResult().poll());
        }
        System.out.println("Ended");
    }
}

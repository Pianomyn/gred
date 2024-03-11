package org.pianomyn.traversal;

import java.io.File;
import java.util.Queue;
import java.util.LinkedList;

public class BFS {
    private Queue<String> bfsQueue;
    private String root;

    public BFS(String root) {
        this.root = root;
        this.bfsQueue = new LinkedList<>();
    }

    // Getters
    public String getRoot() {
        return this.root;
    }

    // Setters
    public void setRoot(String newRoot) {
        this.root = newRoot;
    }

    // Instance methods
    public Queue<String> traverse(String root) {
        Queue<String> filePaths = new LinkedList<String>();
        File currentFile = new File(this.root);
        if (currentFile.exists()) {
            this.bfsQueue.offer(this.root);
        }

        while (!this.bfsQueue.isEmpty()) {
            this.exploreCurrentDepth(filePaths);
        }
        //Queue<String> result = this.filePaths;
        //this.filePaths.clear();
        return filePaths;
    }

    private void exploreCurrentDepth(Queue<String> filePaths) {
        int n = this.bfsQueue.size();
        String currentName;
        File currentFile;
        while (n > 0) {
            currentName = this.bfsQueue.poll();
            this.enqueueChildren(filePaths, currentName);
            n--;
        }
    }

    private void enqueueChildren(Queue<String> filePaths, String currentName) {
        File currentFile = new File(currentName);
        if (currentFile.isDirectory()) {
            for (File child : currentFile.listFiles()) {
                this.bfsQueue.offer(currentName + "/" + child.getName());
            }
        } else if (currentFile.isFile()) {
            filePaths.offer(currentName);
        }
    }

    public static void main(String[] args) {
        BFS t = new BFS("/home/andi/mydir");
        System.out.println("Started");
        Queue<String> filePaths = t.traverse(t.getRoot());
        while (!filePaths.isEmpty()) {
            System.out.println(filePaths.poll());
        }
        System.out.println("Ended");
    }
}

package org.pianomyn.matching;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class MatchingAlgorithm {
    private String filePath;
    private String pattern;
    public MatchingAlgorithm(String filePath, String pattern) {
        this.filePath = filePath;
        this.pattern = pattern;
    }
    public abstract List<List<Integer>> findMatches();
    public boolean fileExists() {
        File file = new File(this.filePath);
        return file.exists();
    }
}

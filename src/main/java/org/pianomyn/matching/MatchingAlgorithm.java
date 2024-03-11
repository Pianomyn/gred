package org.pianomyn.matching;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class MatchingAlgorithm {
    protected String filePath;
    protected String pattern;
    public MatchingAlgorithm(String filePath, String pattern) {
        this.filePath = filePath;
        this.pattern = pattern;
    }
    public abstract List<List<Integer>> findMatches();  // [[line, starting index]]
    public boolean fileExists() {
        File file = new File(this.filePath);
        return file.exists();
    }
}

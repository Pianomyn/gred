package org.pianomyn.gred.matching;

import org.pianomyn.gred.reading.LineReader;

import java.nio.file.Path;
import java.util.List;

public class KMP extends MatchingAlgorithm {
    public KMP(LineReader reader, String pattern) {
        super(reader, pattern);
    }

    private int[] constructPrefix() {
        return new int[]{};
    }

    @Override
    public List<List<Integer>> findMatches() {
        return List.of();
    }
}

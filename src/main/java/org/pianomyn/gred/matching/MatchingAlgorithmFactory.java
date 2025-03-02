package org.pianomyn.gred.matching;

import org.pianomyn.gred.reading.LineReader;

public class MatchingAlgorithmFactory {
    public static MatchingAlgorithm create(MatchingAlgorithmType algorithmType, LineReader lineReader, String pattern) {
        switch (algorithmType) {
            case BOYER_MOORE:
                return new BoyerMoore(lineReader, pattern);
            case KMP:
                return new KMP(lineReader, pattern);
            case NAIVE:
                return new BoyerMoore(lineReader, pattern);
            case RABIN_KARP:
                return new BoyerMoore(lineReader, pattern);
            default:
                throw new IllegalArgumentException("Unknown algorithm type: " + algorithmType);
        }
    }
}

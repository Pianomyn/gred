package org.pianomyn.matching;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class RabinKarp extends MatchingAlgorithm {
    private String filePath;
    private String pattern;
    private int HASH_CONSTANT = 101;  // Prime number close to size of alphabet (ASCII-128 in this instance).
    public RabinKarp(String filePath, String pattern) {
        super(filePath, pattern);
    }

    @Override
    public List<List<Integer>> findMatches() {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if(!this.fileExists()) {
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                // Rabin Karp here;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

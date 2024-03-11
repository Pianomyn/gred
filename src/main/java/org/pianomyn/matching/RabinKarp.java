package org.pianomyn.matching;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RabinKarp extends MatchingAlgorithm {
    //private String filePath;
    //private String pattern;
    private int BASE = 256;  // Size of alphabet
    private int PRIME = 101;  // Prime number close to size of alphabet (ASCII-256 in this instance).

    public RabinKarp(String filePath, String pattern) {
        super(filePath, pattern);
    }

    public RabinKarp(String filePath, String pattern, int base, int prime) {
        super(filePath, pattern);
        this.BASE = base;
        this.PRIME = prime;
    }

    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public List<List<Integer>> findMatches() {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (!this.fileExists()) {
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                // Rabin Karp here;
                // TODO: Case where line shorter than pattern?
                int m = this.pattern.length();
                int n = line.length();
                int patternHash = hash(pattern, m);
                int textHash = hash(line.substring(0, m), m);

                for (int i = 0; i <= n - m; i++) {
                    if (patternHash == textHash && checkEqual(line, pattern, i, i + m - 1)) {
                        result.add(Arrays.asList(lineNumber, i));
                    }
                    if (i < n - m) {
                        textHash = recomputeHash(line, i, i + m, textHash, m);
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private int hash(String str, int length) {
        int hashValue = 0;
        for (int i = 0; i < length; i++) {
            hashValue = (hashValue * this.BASE + str.charAt(i)) % this.PRIME;
        }
        return hashValue;
    }

    private int recomputeHash(String str, int oldIndex, int newIndex, int oldHash, int patternLength) {
        int newHash = oldHash - str.charAt(oldIndex) * (int) Math.pow(this.BASE, patternLength - 1);
        newHash = (newHash * this.BASE + str.charAt(newIndex)) % this.PRIME;
        return (newHash < 0) ? (newHash + this.PRIME) : newHash; // Ensure the hash value is positive
    }


    private static boolean checkEqual(String text, String pattern, int start, int end) {
        int pIndex = 0;
        for (int i = start; i <= end; i++) {
            if (text.charAt(i) != pattern.charAt(pIndex++)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        RabinKarp r = new RabinKarp(System.getProperty("user.home") + "/mydir/a.py", "if");
        for (List<Integer> match : r.findMatches()) {
            for (int i : match) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}

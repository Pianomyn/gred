#!/bin/bash
# set -e

DEPTH=3
WIDTH=2
FILES_PER_DIR=4
FILE_SIZE_MB=1
BASE_DIR="./benchmarks/testdata"
RESULTS_DIR="./benchmarks/results"
MYGREP_JAR="build/libs/gred-1.0-SNAPSHOT-all.jar"
SEARCH_PATTERN="search_pattern"

mkdir -p "$RESULTS_DIR"

echo "Generating test data..."
python3 ./benchmarks/generate_ascii_files.py $DEPTH $WIDTH $FILES_PER_DIR $FILE_SIZE_MB "$BASE_DIR"

echo "Running system grep benchmark..."
START=$(date +%s.%N)
grep -r "$SEARCH_PATTERN" "$BASE_DIR" > /dev/null
END=$(date +%s.%N)
ELAPSED_SYSTEM=$(echo "$END - $START" | bc)

if [ ! -f "$RESULTS_DIR/system_grep_results.csv" ]; then
  echo "depth,width,files_per_dir,file_size_mb,elapsed_time_sec" >> "$RESULTS_DIR/mygrep_results.csv"
fi
echo "$DEPTH,$WIDTH,$FILES_PER_DIR,$FILE_SIZE_MB,$ELAPSED_SYSTEM" >> "$RESULTS_DIR/system_grep_results.csv"

echo "Running mygrep (Java) benchmark..."
START=$(date +%s.%N)
java -jar "$MYGREP_JAR" "$SEARCH_PATTERN" "$BASE_DIR" > /dev/null
END=$(date +%s.%N)
ELAPSED_MYGREP=$(echo "$END - $START" | bc)

if [ ! -f "$RESULTS_DIR/mygrep_results.csv" ]; then
  echo "depth,width,files_per_dir,file_size_mb,elapsed_time_sec" >> "$RESULTS_DIR/mygrep_results.csv"
fi
echo "$DEPTH,$WIDTH,$FILES_PER_DIR,$FILE_SIZE_MB,$ELAPSED_MYGREP" >> "$RESULTS_DIR/mygrep_results.csv"

echo "Done. Results saved in $RESULTS_DIR."
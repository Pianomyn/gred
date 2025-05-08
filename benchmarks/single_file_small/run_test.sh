# Common = Alice
# Uncommon = crocodile
PATTERN="Alice"

echo "Bencharking Java"
time java -jar build/libs/gred-1.0-SNAPSHOT-all.jar PATTERN "benchmarks/single_file_small/alice29.txt" > /dev/null

echo "Benchmarking Grep"
time grep PATTERN "benchmarks/single_file_small/alice29.txt" > /dev/null
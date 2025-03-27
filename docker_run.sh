# docker build -t gred-docker

if [ -z "$1" ]; then
  echo "Usage: $0 \"pattern\" \"/path/to/search\""
  exit 1
fi

PATTERN=$1
DIRECTORY=$2

if [ -z "$DIRECTORY" ]; then
  DIRECTORY="."
fi

docker run -v "$(pwd)/$DIRECTORY:/app/data" gred-docker "$PATTERN" /app/data
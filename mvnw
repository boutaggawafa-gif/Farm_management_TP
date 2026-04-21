#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
PROPS_FILE="$SCRIPT_DIR/.mvn/wrapper.properties"

if [ ! -f "$PROPS_FILE" ]; then
  echo "Missing wrapper properties: $PROPS_FILE" >&2
  exit 1
fi

MAVEN_VERSION=$(grep '^maven.version=' "$PROPS_FILE" | cut -d'=' -f2-)
MAVEN_BASE_URL=$(grep '^maven.baseUrl=' "$PROPS_FILE" | cut -d'=' -f2-)

if [ -z "${MAVEN_VERSION:-}" ] || [ -z "${MAVEN_BASE_URL:-}" ]; then
  echo "Missing Maven wrapper configuration in $PROPS_FILE" >&2
  exit 1
fi

MAVEN_DIR="$SCRIPT_DIR/.mvn/apache-maven-$MAVEN_VERSION"
MAVEN_TGZ="$SCRIPT_DIR/.mvn/apache-maven.tar.gz"
MAVEN_URL="$MAVEN_BASE_URL/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz"

if [ ! -x "$MAVEN_DIR/bin/mvn" ]; then
  mkdir -p "$SCRIPT_DIR/.mvn"
  echo "Downloading Apache Maven $MAVEN_VERSION..."
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$MAVEN_URL" -o "$MAVEN_TGZ"
  elif command -v wget >/dev/null 2>&1; then
    wget -qO "$MAVEN_TGZ" "$MAVEN_URL"
  else
    echo "curl or wget is required to download Maven." >&2
    exit 1
  fi
  tar -xzf "$MAVEN_TGZ" -C "$SCRIPT_DIR/.mvn"
fi

exec "$MAVEN_DIR/bin/mvn" "$@"

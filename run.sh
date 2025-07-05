```bash
#!/bin/bash
PORT=${PORT:-8080}
ARTIFACT_ID="leaderboard-api"
VERSION="1.0-SNAPSHOT"
JAR_FILE="target/${ARTIFACT_ID}-${VERSION}.jar"

echo "🚀 Starting server on port $PORT..."
if [ -f "$JAR_FILE" ]; then
    java -Dserver.port=$PORT -jar "$JAR_FILE"
else
    echo "❌ JAR file not found: $JAR_FILE"
    echo "📁 Available files in target/:"
    ls target/
    exit 1
fi
```
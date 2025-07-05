@echo off
setlocal

set PORT=%PORT%
if "%PORT%"=="" set PORT=8080
set ARTIFACT_ID=leaderboard-api
set VERSION=1.0-SNAPSHOT
set JAR_FILE=target\%ARTIFACT_ID%-%VERSION%.jar

echo 🚀 Starting server on port %PORT%...
if exist "%JAR_FILE%" (
    java -Dserver.port=%PORT% -jar "%JAR_FILE%"
) else (
    echo ❌ JAR file not found: %JAR_FILE%
    echo 📁 Available files in target:
    dir target
    exit /b 1
)

endlocal
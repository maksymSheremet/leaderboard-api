@echo off
echo 📦 Cleaning and building project with Maven...
mvn clean package

if errorlevel 1 (
    echo ❌ Build failed!
    exit /b 1
) else (
    echo ✅ Build complete!
)

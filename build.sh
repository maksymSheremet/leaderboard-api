```bash
#!/bin/bash
echo "📦 Cleaning and building project with Maven..."
mvn clean package
if [ $? -eq 0 ]; then
    echo "✅ Build complete!"
else
    echo "❌ Build failed!"
    exit 1
fi
```
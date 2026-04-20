#!/bin/bash
echo "Compiling Stock Portfolio Manager..."
mkdir -p out
javac -d out src/*.java
if [ $? -eq 0 ]; then
    echo "Compilation successful! Starting app..."
    java -cp out StockPortfolioApp
else
    echo "Compilation failed. Please check errors above."
fi

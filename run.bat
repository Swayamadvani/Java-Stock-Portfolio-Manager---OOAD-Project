@echo off
echo Compiling Stock Portfolio Manager...
if not exist out mkdir out
javac -d out src\*.java
if %errorlevel% == 0 (
    echo Compilation successful! Starting app...
    java -cp out StockPortfolioApp
) else (
    echo Compilation failed. Please check errors above.
)
pause

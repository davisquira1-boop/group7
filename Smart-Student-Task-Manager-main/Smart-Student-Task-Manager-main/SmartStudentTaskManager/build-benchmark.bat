@echo off
REM Build for BenchmarkAndPresentationDriver
cd /d "%~dp0"
if not exist bin mkdir bin
javac -d bin src\BenchmarkAndPresentationDriver.java src\BaselineTaskController.java src\BaselineTaskManagerGUI.java src\TaskController.java src\TaskManagerGUI.java src\Main.java src\baseline\*.java src\optimized\*.java
if errorlevel 1 (
  echo Build failed.
  exit /b 1
)
echo Build complete for benchmark.

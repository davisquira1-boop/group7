@echo off
cd /d "%~dp0"
if not exist bin mkdir bin
javac -d bin src\*.java src\baseline\*.java src\optimized\*.java
if errorlevel 1 (
  echo Build failed.
  exit /b 1
)
echo Build complete.

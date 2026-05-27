@echo off
cd /d "%~dp0"
if not exist bin (
  echo Bin folder missing. Run build-benchmark.bat first.
  exit /b 1
)
java -cp bin BenchmarkAndPresentationDriver

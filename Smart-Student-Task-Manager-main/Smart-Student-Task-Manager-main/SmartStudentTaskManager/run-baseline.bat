@echo off
cd /d "%~dp0"
if not exist bin (
  echo Bin folder missing. Run build-baseline.bat first.
  exit /b 1
)
java -cp bin BaselineMain

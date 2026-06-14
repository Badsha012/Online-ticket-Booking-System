@echo off
setlocal
cd /d "%~dp0"
javac -cp ".;mysql-connector-j-9.7.0.jar" *.java
if errorlevel 1 pause & exit /b 1
java -cp ".;mysql-connector-j-9.7.0.jar" MainDashboard
pause

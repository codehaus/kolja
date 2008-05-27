@echo off

setlocal
call "%~dp0_setenv.bat"

java -Djava.awt.headless=true -classpath "%CP%" com.baulsupp.kolja.ansi.reports.ReportRunnerMain %*
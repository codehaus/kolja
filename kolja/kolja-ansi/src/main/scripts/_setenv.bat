@echo off

if not "%JAVA_HOME%" == "" goto gotJava
echo You must set JAVA_HOME to point at your JRE/JDK installation
exit /b 1
:gotJava

set KOLJA_HOME=%~dp0

for %%i in ("%KOLJA_HOME%\lib\*.jar") do @call :setcp %%i

goto :EOF

rem Setcp subroutine
:setcp
if not "%CP%" == "" goto add

set CP=%*
goto :EOF

:add
set CP=%CP%;%*

goto :EOF
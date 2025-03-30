@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM compile the code into the bin folder
cd ..
javac -cp src/main/java -Xlint:none -d bin src/main/java/*.java src/main/java/*/*.java src/main/java/*/*/*.java
IF ERRORLEVEL 1 (
    echo Compilation failed
    exit /b 1
)

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath bin -ea clinicease.ClinicEase < text-ui-test/input.txt > text-ui-test/ACTUAL.TXT

REM compare the output to the expected output
cd text-ui-test
FC ACTUAL.TXT EXPECTED.TXT
IF ERRORLEVEL 1 (
    echo Test result: FAILED
    exit /b 1
) else (
    echo Test result: PASSED
    exit /b 0
)

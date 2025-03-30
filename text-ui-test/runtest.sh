#!/usr/bin/env bash

# create bin directory if it doesn't exist
mkdir -p ../bin

# delete output from previous run
if [ -e ACTUAL.TXT ]
then
    rm ACTUAL.TXT
fi

# compile the code into the bin folder
cd ..
javac -cp src/main/java -Xlint:none -d bin src/main/java/*.java src/main/java/*/*.java src/main/java/*/*/*.java

# run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath bin -ea clinicease.ClinicEase < text-ui-test/input.txt > text-ui-test/ACTUAL.TXT

# compare the output to the expected output
cd text-ui-test
diff ACTUAL.TXT EXPECTED.TXT
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi
